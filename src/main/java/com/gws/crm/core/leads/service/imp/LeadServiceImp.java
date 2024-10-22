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
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.mapper.LeadMapper;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.repository.PhoneNumberRepository;
import com.gws.crm.core.leads.service.LeadService;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.lookups.service.LeadLookupsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.created;
import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;
import static com.gws.crm.core.leads.specification.LeadSpecification.filter;

@Service
@Slf4j
@RequiredArgsConstructor
public class LeadServiceImp implements LeadService {

    private final LeadRepository leadRepository;
    private final BaseLeadRepository baseLeadRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final InvestmentGoalRepository investmentGoalRepository;
    private final CommunicateWayRepository communicateWayRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final EmployeeRepository employeeRepository;
    private final ChannelRepository channelRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LeadMapper leadMapper;
    private final PhoneNumberMapper phoneNumberMapper;
    private final LeadLookupsService leadLookupsService;
    private final ExcelSheetService excelSheetService;
    private final BrokerRepository brokerRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    @Override
    public ResponseEntity<?> getLeadDetails(long leadId, Transition transition) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(NotFoundResourceException::new);
        LeadResponse leadResponse  = leadMapper.toDTO(lead);
        return success(leadResponse);
    }


    @Override
    public ResponseEntity<?> addLead(AddLeadDTO leadDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        Admin admin = null;

        Lead.LeadBuilder leadBuilder = Lead.builder()
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
                .campaignId(leadDTO.getCampaignId())
                .lastStage(leadDTO.getLastStage())
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

        if(leadDTO.getBroker() != null){
            leadBuilder.broker(brokerRepository.getReferenceById(leadDTO.getBroker()));
        }

        Lead lead = leadBuilder.build();
        List<PhoneNumber> phoneNumbers = phoneNumberMapper.toEntityList(leadDTO.getPhoneNumbers(), lead);
        lead.setPhoneNumbers(phoneNumbers);
        Lead savedLead = leadRepository.save(lead);
        LeadResponse leadResponse = leadMapper.toDTO(savedLead);
        return created(leadResponse);
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> updateLead(AddLeadDTO leadDTO, Transition transition) {
        Lead existingLead = leadRepository.findById(leadDTO.getId())
                .orElseThrow(NotFoundResourceException::new);

        User creator = userRepository.findById(transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        Admin admin = null;

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
            existingLead.setAdmin(admin);
        } else {
            existingLead.setAdmin((Admin) creator);
        }

        existingLead.setName(leadDTO.getName());
        existingLead.setStatus(leadStatusRepository.getReferenceById(leadDTO.getStatus()));
        existingLead.setCountry(leadDTO.getCountry());
        existingLead.setContactTime(leadDTO.getContactTime());
        existingLead.setWhatsappNumber(leadDTO.getWhatsappNumber());
        existingLead.setEmail(leadDTO.getEmail());
        existingLead.setJobTitle(leadDTO.getJobTitle());
        existingLead.setUpdatedAt(LocalDateTime.now());
        existingLead.setBudget(leadDTO.getBudget());
        existingLead.setNote(leadDTO.getNote());
        existingLead.setDeleted(false);
        existingLead.setCampaignId(leadDTO.getCampaignId());
        existingLead.setLastStage(leadDTO.getLastStage());
        existingLead.setCreator(creator);

        if (leadDTO.getInvestmentGoal() != null) {
            existingLead.setInvestmentGoal(investmentGoalRepository.getReferenceById(leadDTO.getInvestmentGoal()));
        }

        if (leadDTO.getCommunicateWay() != null) {
            existingLead.setCommunicateWay(communicateWayRepository.getReferenceById(leadDTO.getCommunicateWay()));
        }

        if (leadDTO.getCancelReason() != null) {
            existingLead.setCancelReasons(cancelReasonsRepository.getReferenceById(leadDTO.getCancelReason()));
        }

        if (leadDTO.getSalesRep() != null) {
            existingLead.setSalesRep(employeeRepository.getReferenceById(leadDTO.getSalesRep()));
        }

        if (leadDTO.getChannel() != null) {
            existingLead.setChannel(channelRepository.getReferenceById(leadDTO.getChannel()));
        }

        if (leadDTO.getProject() != null) {
            existingLead.setProject(projectRepository.getReferenceById(leadDTO.getProject()));
        }

        if (leadDTO.getBroker() != null) {
            existingLead.setBroker(brokerRepository.getReferenceById(leadDTO.getBroker()));
        }

        phoneNumberRepository.deleteAllById(existingLead.getPhoneNumbers().stream().map(PhoneNumber::getId).toList());

        existingLead.getPhoneNumbers().clear();

        List<PhoneNumber> updatedPhoneNumbers = phoneNumberMapper.toEntityList(leadDTO.getPhoneNumbers(), existingLead);
        existingLead.setPhoneNumbers(updatedPhoneNumbers);

        Lead updatedLead = leadRepository.save(existingLead);

        LeadResponse leadResponse = leadMapper.toDTO(updatedLead);

        return ResponseEntity.ok(leadResponse);
    }

    @Override
    public ResponseEntity<?> deleteLead(long leadId, Transition transition) {
        baseLeadRepository.deleteLead(leadId);
        return success("Lead Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> getLeads(LeadCriteria leadCriteria, Transition transition) {
        Specification<Lead> leadSpecification = filter(leadCriteria, transition);
        Pageable pageable = PageRequest.of(leadCriteria.getPage(), leadCriteria.getSize());
        Page<Lead> leadPage = leadRepository.findAll(leadSpecification, pageable);
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
        List<Lead> leadList = createLeadsList(leads, transition);
        leadRepository.saveAll(leadList);
        return success("Lead Imported Successfully");
    }

    private List<Lead> createLeadsList(List<ImportLeadDTO> importLeadDTOS, Transition transition) {
        List<Lead> leads = new ArrayList<>();
        User creator = userRepository.findById(transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        Admin admin;

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
        } else {
            admin = (Admin) creator;
        }

        Admin finalAdmin = admin;

        importLeadDTOS.forEach(leadDTO -> {
            Lead.LeadBuilder leadBuilder = Lead.builder()
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

            Lead lead = leadBuilder.build();

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
