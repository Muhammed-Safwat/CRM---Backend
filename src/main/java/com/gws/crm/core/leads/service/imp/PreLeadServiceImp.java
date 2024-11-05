package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.handler.ApiResponseHandler;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.employee.service.imp.ActionServiceImp;
import com.gws.crm.core.employee.service.imp.LeadActionService;
import com.gws.crm.core.leads.dto.*;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.mapper.PreLeadMapper;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.repository.PreLeadRepository;
import com.gws.crm.core.leads.service.PreLeadService;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import com.gws.crm.core.lookups.repository.ChannelRepository;
import com.gws.crm.core.lookups.repository.LeadStatusRepository;
import com.gws.crm.core.lookups.repository.ProjectRepository;
import com.gws.crm.core.lookups.service.LeadLookupsService;
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
import java.util.HashMap;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;
import static com.gws.crm.core.leads.specification.PreLeadSpecification.filter;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreLeadServiceImp implements PreLeadService {

    private final PreLeadRepository preLeadRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PhoneNumberMapper phoneNumberMapper;
    private final PreLeadMapper preLeadMapper;
    private final ProjectRepository projectRepository;
    private final ChannelRepository channelRepository;
    private final BaseLeadRepository baseLeadRepository;
    private final LeadLookupsService leadLookupsService;
    private final ExcelSheetService excelSheetService;
    private final AdminRepository adminRepository;
    private final LeadRepository leadRepository ;
    private final LeadActionService actionServiceImp;
    private final LeadStatusRepository leadStatusRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> getAllPreLead(PreLeadCriteria preLeadCriteria, Transition transition) {
        Specification<PreLead> leadSpecification = filter(preLeadCriteria, transition);
        Pageable pageable = PageRequest.of(preLeadCriteria.getPage(), preLeadCriteria.getSize());
        Page<PreLead> leadPage = preLeadRepository.findAll(leadSpecification, pageable);
        Page<PreLeadResponse> leadResponses = preLeadMapper.toDTOPage(leadPage);
        return success(leadResponses);
    }

    @Override
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
                .build();

        List<PhoneNumber> phoneNumbers = phoneNumberMapper.toEntityList(preLeadDTO.getPhoneNumbers(), preLead);

        preLead.setPhoneNumbers(phoneNumbers);
        preLeadRepository.save(preLead);
        return ApiResponseHandler.success("Pre Lead added successfully");
    }

    @Override
    public ResponseEntity<?> deletePreLead(Long leadId, Transition transition) {
        baseLeadRepository.deleteLead(leadId);
        return success("Lead Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> restorePreLead(Long leadId, Transition transition) {
        baseLeadRepository.restoreLead(leadId);
        return success("Lead Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> importLead(List<ImportPreLeadDTO> leads, Transition transition) {
        List<PreLead> leadList = createLeadsList(leads, transition);
        preLeadRepository.saveAll(leadList);
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
    public ResponseEntity<?> assignToSales(AssignToSalesDTO assignToSalesDTO, Transition transition) {
        List<PreLead> preLeads = preLeadRepository.findAllById(assignToSalesDTO.getLeadsIds());
        Admin admin = adminRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        List<Lead> leads = new ArrayList<>();
        preLeads.forEach(lead ->{
            lead.setImported(true);
            leads.add(toSalesLead(lead,admin,transition));
        });
        leadRepository.saveAll(leads);
        preLeadRepository.saveAll(preLeads);
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
        HashMap<String,Boolean> body = new HashMap<>();
        body.put("isExists",exists);
        return success(body);
    }


    public Lead toSalesLead(PreLead preLeads,Admin admin,Transition transition) {

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
                .build();
        List<PhoneNumber> phoneNumbers = preLeads.getPhoneNumbers().stream().map(num ->{
            return PhoneNumber.builder().phone(num.getPhone()).code(num.getCode()).lead(lead).build();
        }).toList();
        lead.setPhoneNumbers(phoneNumbers);
        actionServiceImp.setActionForImportedPreLead(lead,transition);
        return lead;
    }
}
