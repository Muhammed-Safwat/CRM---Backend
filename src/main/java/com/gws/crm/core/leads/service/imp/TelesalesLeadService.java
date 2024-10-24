package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.employee.service.imp.ActionServiceImp;
import com.gws.crm.core.employee.service.imp.TeleSalesLeadActionService;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.mapper.TeleSalesLeadMapper;
import com.gws.crm.core.leads.repository.PhoneNumberRepository;
import com.gws.crm.core.leads.repository.TeleSalesLeadRepository;
import com.gws.crm.core.lookups.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;

@Service
@Slf4j
public class TelesalesLeadService extends SalesLeadServiceImp<TeleSalesLead, AddLeadDTO> {

    private final TeleSalesLeadRepository leadRepository;
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
    private final PhoneNumberRepository phoneNumberRepository;
    private final BrokerRepository brokerRepository;
    private final  ActionServiceImp<TeleSalesLead> leadActionService;

    protected TelesalesLeadService(TeleSalesLeadRepository leadRepository, LeadStatusRepository leadStatusRepository
            , InvestmentGoalRepository investmentGoalRepository, CommunicateWayRepository communicateWayRepository, CancelReasonsRepository cancelReasonsRepository, EmployeeRepository employeeRepository, ChannelRepository channelRepository, ProjectRepository projectRepository, UserRepository userRepository, TeleSalesLeadMapper leadMapper, PhoneNumberMapper phoneNumberMapper, ExcelSheetService excelSheetService, PhoneNumberRepository phoneNumberRepository, BrokerRepository brokerRepository, ActionServiceImp<TeleSalesLead> leadActionService) {
        super(leadRepository, leadActionService ,employeeRepository);
        this.leadRepository = leadRepository;
        this.leadStatusRepository = leadStatusRepository;
        this.investmentGoalRepository = investmentGoalRepository;
        this.communicateWayRepository = communicateWayRepository;
        this.cancelReasonsRepository = cancelReasonsRepository;
        this.employeeRepository = employeeRepository;
        this.channelRepository = channelRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.leadMapper = leadMapper;
        this.phoneNumberMapper = phoneNumberMapper;
        this.excelSheetService = excelSheetService;
        this.phoneNumberRepository = phoneNumberRepository;
        this.brokerRepository = brokerRepository;
        this.leadActionService = leadActionService;
    }

    @Override
    protected TeleSalesLead mapDtoToEntity(AddLeadDTO leadDTO, Transition transition) {
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

        if (leadDTO.getBroker() != null) {
            leadBuilder.broker(brokerRepository.getReferenceById(leadDTO.getBroker()));
        }

        TeleSalesLead lead = leadBuilder.build();
        List<PhoneNumber> phoneNumbers = phoneNumberMapper.toEntityList(leadDTO.getPhoneNumbers(), lead);
        lead.setPhoneNumbers(phoneNumbers);
        return lead;
    }

    @Override
    protected LeadResponse mapEntityToDto(TeleSalesLead entity) {
        return leadMapper.toDTO(entity);
    }

    @Override
    protected Page<LeadResponse> mapEntityToDto(Page<TeleSalesLead> entityPage) {
        return leadMapper.toDTOPage(entityPage);
    }

    @Override
    protected void updateEntityFromDto(TeleSalesLead entity, AddLeadDTO leadDTO, Transition transition) {
        TeleSalesLead existingLead = leadRepository.findById(leadDTO.getId())
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

        if (!(existingLead.getId() == leadDTO.getSalesRep())) {
            existingLead.setSalesRep(employeeRepository.getReferenceById(leadDTO.getSalesRep()));
            leadActionService.setSalesAssignAction(existingLead, transition);
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
                    .actions(new ArrayList<>())
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
