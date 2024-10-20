package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.LeadCriteria;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.mapper.TeleSalesLeadMapper;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import com.gws.crm.core.leads.repository.TeleSalesLeadRepository;
import com.gws.crm.core.leads.service.TeleSalesLeadService;
import com.gws.crm.core.lookups.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.created;
import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;
import static com.gws.crm.core.leads.specification.TeleSalesLeadSpecification.filter;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeleSalesLeadServiceImp implements TeleSalesLeadService {

    private final TeleSalesLeadRepository leadRepository;
    private final BaseLeadRepository baseLeadRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final InvestmentGoalRepository investmentGoalRepository;
    private final CommunicateWayRepository communicateWayRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final EmployeeRepository employeeRepository;
    private final ChannelRepository channelRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeleSalesLeadMapper leadMapper;
    private final PhoneNumberMapper phoneNumberMapper;
    private final ExcelSheetService excelSheetService;

    @Override
    public ResponseEntity<?> getLeadDetails(long leadId, Transition transition) {
        return null;
    }


    @Override
    public ResponseEntity<?> addLead(AddLeadDTO leadDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        Admin admin = null;

        TeleSalesLead.TeleSalesLeadBuilder leadBuilder = TeleSalesLead.builder()
                .name(leadDTO.getName())
                .status(leadStatusRepository.getReferenceById(leadDTO.getStatus()))
                .country(leadDTO.getCountry())
                .contactTime(leadDTO.getContactTime())
                .whatsappNumber(leadDTO.getWhatsappNumber())
                .email(leadDTO.getEmail())
                .jobTitle(leadDTO.getJobTitle())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .budget(leadDTO.getBudget())
                .note(leadDTO.getNote())
                .deleted(false)
                .creator(creator);

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
            leadBuilder.admin(admin);
        } else {
            leadBuilder.admin((Admin) creator);
        }

        if (leadDTO.getInvestmentGoal() != null) {
            leadBuilder.investmentGoal(investmentGoalRepository.getReferenceById(leadDTO.getInvestmentGoal()));
        }

        if (leadDTO.getCommunicateWay() != null) {
            leadBuilder.communicateWay(communicateWayRepository.getReferenceById(leadDTO.getCommunicateWay()));
        }

        if (leadDTO.getCancelReason() != null) {
            leadBuilder.cancelReasons(cancelReasonsRepository.getReferenceById(leadDTO.getCancelReason()));
        }

        if (leadDTO.getSalesRep() != null) {
            leadBuilder.salesRep(employeeRepository.getReferenceById(leadDTO.getSalesRep()));
        }

        if (leadDTO.getChannel() != null) {
            leadBuilder.channel(channelRepository.getReferenceById(leadDTO.getChannel()));
        }

        if (leadDTO.getProject() != null) {
            leadBuilder.project(projectRepository.getReferenceById(leadDTO.getProject()));
        }

        TeleSalesLead lead = leadBuilder.build();
        List<PhoneNumber> phoneNumbers = phoneNumberMapper.toEntityList(leadDTO.getPhoneNumbers(), lead);
        lead.setPhoneNumbers(phoneNumbers);
        TeleSalesLead savedLead = leadRepository.save(lead);
        LeadResponse leadResponse = leadMapper.toDTO(savedLead);
        return created(leadResponse);
    }

    @Override
    public ResponseEntity<?> updateLead(AddLeadDTO leadDTO, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteLead(long leadId, Transition transition) {
        baseLeadRepository.deleteLead(leadId);
        return success("Lead Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> getLeads(LeadCriteria leadCriteria, Transition transition) {
        Specification<TeleSalesLead> leadSpecification = filter(leadCriteria, transition);
        Pageable pageable = PageRequest.of(leadCriteria.getPage(), leadCriteria.getSize());
        Page<TeleSalesLead> leadPage = leadRepository.findAll(leadSpecification, pageable);
        Page<LeadResponse> leadResponses = leadMapper.toDTOPage(leadPage);
        return success(leadResponses);
    }

    @Override
    public ResponseEntity<?> restoreLead(Long leadId, Transition transition) {
        baseLeadRepository.restoreLead(leadId);
        return success("Lead restored Successfully");
    }

    @Override
    public ResponseEntity<?> generateExcel(Transition transition) {
        ExcelFile excelFile = ExcelFile.builder()
                .header(generateHeader(AddLeadDTO.class))
                .dropdowns(excelSheetService.generateLeadExcelSheetMap(transition)).build();
        return success(excelFile);
    }

    @Override
    public ResponseEntity<?> importLead(List<ImportLeadDTO> leads, Transition transition) {
        List<TeleSalesLead> leadList = createLeadsList(leads, transition);
        leadRepository.saveAll(leadList);
        return success("Lead Imported Successfully");
    }

    private List<TeleSalesLead> createLeadsList(List<ImportLeadDTO> importLeadDTOS, Transition transition) {
        List<TeleSalesLead> leads = new ArrayList<>();
        User creator = userRepository.findById(transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        Admin admin;

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
        } else {
            admin = (Admin) creator;
        }

        Admin finalAdmin = admin;

        importLeadDTOS.forEach(leadDTO -> {
            TeleSalesLead.TeleSalesLeadBuilder leadBuilder = TeleSalesLead.builder()
                    .name(leadDTO.getName())
                    .creator(creator)
                    .admin(finalAdmin)
                    .budget(leadDTO.getBudget())
                    .note(leadDTO.getNote())
                    .country(leadDTO.getCountry())
                    .deleted(false)
                    .email(leadDTO.getEmail())
                    .whatsappNumber(leadDTO.getWhatsappNumber())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .status(leadStatusRepository.findByName(leadDTO.getStatus()));

            if (leadDTO.getInvestmentGoal() != null) {
                leadBuilder.investmentGoal(investmentGoalRepository.findByNameAndAdminId(leadDTO.getInvestmentGoal(), finalAdmin.getId()));
            }

            if (leadDTO.getCommunicateWay() != null) {
                leadBuilder.communicateWay(communicateWayRepository.findByNameAndAdminId(leadDTO.getCommunicateWay(), finalAdmin.getId()));
            }

            if (leadDTO.getCancelReason() != null) {
                leadBuilder.cancelReasons(cancelReasonsRepository.findByNameAndAdminId(leadDTO.getCancelReason(), finalAdmin.getId()));
            }

            if (leadDTO.getSalesRep() != null) {
                leadBuilder.salesRep(employeeRepository.findByNameAndAdminId(leadDTO.getSalesRep(), finalAdmin.getId()));
            }

            if (leadDTO.getChannel() != null) {
                leadBuilder.channel(channelRepository.findByNameAndAdminId(leadDTO.getChannel(), finalAdmin.getId()));
            }

            if (leadDTO.getProject() != null) {
                leadBuilder.project(projectRepository.findByNameAndAdminId(leadDTO.getProject(), finalAdmin.getId()));
            }

            log.info("Project {} ", leadDTO.getProject());

            TeleSalesLead lead = leadBuilder.build();

            PhoneNumber phoneNumber = PhoneNumber.builder()
                    .lead(lead)
                    .phone(leadDTO.getPhoneNumbers())
                    .build();

            lead.setPhoneNumbers(List.of(phoneNumber));

            leads.add(lead);
        });

        return leads;
    }

}
