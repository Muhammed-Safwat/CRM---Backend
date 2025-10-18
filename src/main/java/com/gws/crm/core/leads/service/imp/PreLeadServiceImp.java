package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.handler.ApiResponseHandler;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.actions.event.lead.LeadsCreatedEvent;
import com.gws.crm.core.actions.event.prelead.*;
import com.gws.crm.core.employee.entity.Admin;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.*;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.mapper.PreLeadMapper;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.repository.PreLeadRepository;
import com.gws.crm.core.leads.service.PreLeadService;
import com.gws.crm.core.lookups.repository.ChannelRepository;
import com.gws.crm.core.lookups.repository.LeadStatusRepository;
import com.gws.crm.core.lookups.repository.ProjectRepository;
import com.gws.crm.core.notification.publisher.PreLeadNotificationEventPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;
import static com.gws.crm.core.leads.specification.PreLeadSpecification.filter;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PreLeadServiceImp implements PreLeadService {

    private final PreLeadRepository preLeadRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PhoneNumberMapper phoneNumberMapper;
    private final PreLeadMapper preLeadMapper;
    private final ProjectRepository projectRepository;
    private final ChannelRepository channelRepository;
    private final ExcelSheetService excelSheetService;
    private final AdminRepository adminRepository;
    private final LeadRepository leadRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PreLeadNotificationEventPublisher preLeadNotificationEventPublisher;

    @Override
    public ResponseEntity<?> getAllPreLead(PreLeadCriteria preLeadCriteria, Transition transition) {
        if (transition.getRole().equals("USER")) {
            Employee employee =
                    employeeRepository.findByIdWithSubordinates(transition.getUserId())
                            .orElseThrow(NotFoundResourceException::new);
            preLeadCriteria.setSubordinates(employee.getSubordinates()
                    .stream().map(User::getId).toList());
        }
        Specification<PreLead> leadSpecification = filter(preLeadCriteria, transition);
        Pageable pageable = PageRequest.of(preLeadCriteria.getPage(), preLeadCriteria.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PreLead> leadPage = preLeadRepository.findAll(leadSpecification, pageable);
        Page<PreLeadResponse> leadResponses = preLeadMapper.toSimpleDTOPage(leadPage);
        return success(leadResponses);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addPreLead(AddPreLeadDTO preLeadDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Admin admin;

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
        } else {
            admin = (Admin) creator;
        }

        PreLead preLead = PreLead.builder()
                .name(preLeadDTO.getName())
                .country(preLeadDTO.getCountry())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .note(preLeadDTO.getNote())
                .creator(creator)
                .admin(admin)
                .imported(false)
                .link(preLeadDTO.getLink())
                .project(projectRepository.getReferenceById(preLeadDTO.getProject()))
                .channel(channelRepository.getReferenceById(preLeadDTO.getChannel()))
                .actions(new ArrayList<>())
                .build();

        preLeadDTO.getPhoneNumbers().forEach(phone -> {
            log.info("{} {}", phone.getPhone(), phone.getPhone());
        });

        List<PhoneNumber> phoneNumbers = phoneNumberMapper
                .toEntityList(preLeadDTO.getPhoneNumbers(), preLead);

        preLead.setPhoneNumbers(phoneNumbers);
        PreLead savedPreLead = preLeadRepository.save(preLead);
        publishCreateLeadEvent(savedPreLead, transition);
        return ApiResponseHandler.success("Pre Lead added successfully");
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deletePreLead(Long leadId, Transition transition) {
        PreLead preLead = preLeadRepository.findById(leadId)
                .orElseThrow(NotFoundResourceException::new);

        preLeadRepository.deleteLead(leadId); // error here
        publishDeleteLeadEvent(preLead, transition);
        return success("Lead deleted successfully");
    }

    @Override
    public ResponseEntity<ApiResponse<String>> restorePreLead(Long leadId, Transition transition) {
        PreLead preLead = preLeadRepository.findById(leadId)
                .orElseThrow(NotFoundResourceException::new);
        preLeadRepository.restoreLead(leadId);
        publishRestoreLeadEvent(preLead, transition);
        return success("Lead restored successfully");
    }

    @Override
    public ResponseEntity<?> importLead(List<ImportPreLeadDTO> leads, Transition transition) {
        List<PreLead> leadList = createLeadsList(leads, transition);
        List<PreLead> savedLead = preLeadRepository.saveAll(leadList);
        // preLeadActionService.setImportLeads(savedLead, transition);
        return success("Pre Lead Imported Successfully");
    }

    private List<PreLead> createLeadsList(List<ImportPreLeadDTO> importLeadDTOS, Transition transition) {
        List<PreLead> leads = new ArrayList<>();
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Admin admin;

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
        } else {
            admin = (Admin) creator;
        }

        Admin finalAdmin = admin;

        importLeadDTOS.forEach(leadDTO -> {
            PreLead.PreLeadBuilder leadBuilder = PreLead.builder()
                    .name(leadDTO.getName())
                    .creator(creator)
                    .admin(finalAdmin)
                    .note(leadDTO.getNote())
                    .country(leadDTO.getCountry())
                    .deleted(false)
                    .imported(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now());

            if (leadDTO.getChannel() != null) {
                leadBuilder.channel(channelRepository.findByNameAndAdminId(leadDTO.getChannel(), finalAdmin.getId()));
            }

            if (leadDTO.getProject() != null) {
                leadBuilder.project(projectRepository.findByNameAndAdminId(leadDTO.getProject(), finalAdmin.getId()));
            }

            PreLead lead = leadBuilder.build();

            PhoneNumber phoneNumber = PhoneNumber.builder()
                    .lead(lead)
                    .phone(leadDTO.getPhoneNumbers())
                    .build();

            lead.setPhoneNumbers(List.of(phoneNumber));

            leads.add(lead);
        });

        return leads;
    }

    @Override
    public ResponseEntity<?> generateExcel(Transition transition) {
        ExcelFile excelFile = ExcelFile.builder()
                .header(generateHeader(AddPreLeadDTO.class))
                .dropdowns(excelSheetService.generatePreLeadExcelSheetMap(transition))
                .build();
        return success(excelFile);
    }

    @Override
    @Transactional
    public ResponseEntity<?> assignToSales(AssignToSalesDTO assignToSalesDTO, Transition transition) throws Throwable {
        List<PreLead> preLeads = preLeadRepository.findAllById(assignToSalesDTO.getLeadsIds());
        Admin admin = adminRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Employee sales =
                employeeRepository.findById(assignToSalesDTO.getSalesId())
                        .orElseThrow(NotFoundResourceException::new);
        List<Lead> leads = new ArrayList<>();
        preLeads.forEach(preLead -> {
            preLead.setImported(true);
            preLead.setImportedBy(admin.getName());
            preLead.setImportedAt(LocalDateTime.now());
            preLead.setAssignedTo(sales.getName());
            leads.add(toSalesLead(preLead, admin, sales, transition));
            publishAssignLeadEvent(preLead, transition);
            //preLeadActionService.setAssignAction(preLead, transition);
        });

        List<Lead> leadsList = leadRepository.saveAll(leads);
        //preLeadActionService.setAssignAction(preLeads,transition);
        //preLeadRepository.saveAll(preLeads);
        log.info("{} ================", leadsList.size());
        // here contain problem
        publishLeadsCreatedEvent(leadsList, transition);
        // salesLeadActionServiceImp.setLeadCreationAction(leadsList,transition);
        return success("Pre Lead Assigned Successfully");
    }

    @Override
    public ResponseEntity<?> isPhoneExist(List<String> phones, Transition transition) {
        HashMap<String, Object> responseBody = new HashMap<>();
        List<String> existingPhones = new ArrayList<>();

        for (String phone : phones) {
            boolean exists = leadRepository.isPhoneExist(phone);
            if (exists) {
                existingPhones.add(phone);
            }
        }

        if (!existingPhones.isEmpty()) {
            String message = "The following phone numbers already exist: " + String.join(", ", existingPhones);
            responseBody.put("duplicateExists", true);
            responseBody.put("message", message);
        } else {
            responseBody.put("duplicateExists", false);
            responseBody.put("message", "No duplicate phone numbers found.");
        }

        return success(responseBody);
    }

    @Override
    public ResponseEntity<?> isPhoneExist(String phone, Transition transition) {
        boolean exists = leadRepository.isPhoneExist(phone);
        HashMap<String, Boolean> body = new HashMap<>();
        body.put("isExists", exists);
        return success(body);
    }

    @Override
    public ResponseEntity<?> getDetails(long leadId, Transition transition) {
        PreLead preLead = preLeadRepository.findById(leadId).orElseThrow();
        PreLeadResponse preLeadDto = preLeadMapper.toDTO(preLead);
        return success(preLeadDto);
    }

    @Override
    public ResponseEntity<?> toggleArchive(long leadId, Transition transition) {
        PreLead lead = preLeadRepository.findById(leadId).orElseThrow(NotFoundResourceException::new);

        boolean newArchiveStatus = !lead.isArchive();

        preLeadRepository.toggleArchive(leadId, newArchiveStatus);

        String message = newArchiveStatus ? "Lead Archived Successfully" : "Lead Unarchived Successfully";

        return success(message);
    }

    public Lead toSalesLead(PreLead preLeads, Admin admin, Employee employee, Transition transition) {
        Lead lead = Lead.builder()
                .name(preLeads.getName())
                .admin(admin)
                .project(preLeads.getProject())
                .channel(preLeads.getChannel())
                .creator(admin)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .country(preLeads.getCountry())
                .email(preLeads.getEmail())
                .deleted(false)
                .actions(new ArrayList<>())
                .status(leadStatusRepository.findByName("Fresh"))
                .salesRep(employee)
                .build();
        List<PhoneNumber> phoneNumbers = preLeads.getPhoneNumbers().stream().map(num -> {
            return PhoneNumber.builder().phone(num.getPhone()).code(num.getCode()).lead(lead).build();
        }).toList();
        lead.setPhoneNumbers(phoneNumbers);

        return lead;
    }

    public void publishCreateLeadEvent(PreLead lead, Transition transition) {
        preLeadNotificationEventPublisher.publishCreateLeadEvent(lead, transition);
        eventPublisher.publishEvent(new PreLeadCreatedEvent(lead, transition));
    }

    public void publishDeleteLeadEvent(PreLead lead, Transition transition) {
        preLeadNotificationEventPublisher.publishDeleteLeadEvent(lead, transition);
        eventPublisher.publishEvent(new PreLeadDeletedEvent(lead, transition));
    }

    public void publishEditLeadEvent(PreLead lead, Transition transition) {
        preLeadNotificationEventPublisher.publishEditLeadEvent(lead, transition);
        eventPublisher.publishEvent(new PreLeadEditedEvent(lead, transition));
    }

    public void publishRestoreLeadEvent(PreLead lead, Transition transition) {
        preLeadNotificationEventPublisher.publishRestoreLeadEvent(lead, transition);
        eventPublisher.publishEvent(new PreLeadRestoredEvent(lead, transition));
    }

    public void publishAssignLeadEvent(PreLead lead, Transition transition) {
        eventPublisher.publishEvent(new PreLeadAssignedEvent(lead, transition));
    }

    public void publishViewLeadEvent(PreLead lead, Transition transition) {
        eventPublisher.publishEvent(new PreLeadViewedEvent(lead, transition));
    }

    public void publishDelayLeadEvent(PreLead lead, Transition transition) {
        eventPublisher.publishEvent(new PreLeadDelayedEvent(lead, transition));
    }

    public void publishLeadsCreatedEvent(List<Lead> leads, Transition transition) {
        eventPublisher.publishEvent(new LeadsCreatedEvent(leads, transition));
    }
}
