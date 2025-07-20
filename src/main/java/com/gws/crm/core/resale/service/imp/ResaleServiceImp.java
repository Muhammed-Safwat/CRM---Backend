package com.gws.crm.core.resale.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.AssignDTO;
import com.gws.crm.core.leads.dto.AssignResponse;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import com.gws.crm.core.lookups.repository.ProjectRepository;
import com.gws.crm.core.lookups.repository.PropertyTypeRepository;
import com.gws.crm.core.resale.dto.AddResaleDTO;
import com.gws.crm.core.resale.dto.ImportResaleDTO;
import com.gws.crm.core.resale.dto.ResaleCriteria;
import com.gws.crm.core.resale.dto.ResaleResponse;
import com.gws.crm.core.resale.entities.Resale;
import com.gws.crm.core.resale.entities.ResaleStatus;
import com.gws.crm.core.resale.mapper.ResaleMapper;
import com.gws.crm.core.resale.repository.ResaleRepository;
import com.gws.crm.core.resale.repository.ResaleStatusRepository;
import com.gws.crm.core.resale.repository.ResaleTypeRepository;
import com.gws.crm.core.resale.service.ResaleService;
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
import java.util.Objects;

import static com.gws.crm.common.handler.ApiResponseHandler.badRequest;
import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;
import static com.gws.crm.core.resale.specification.ResaleSpecification.filter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResaleServiceImp implements ResaleService {

    private final ResaleRepository resaleRepository;
    private final ResaleMapper resaleMapper;
    private final CategoryRepository categoryRepository;
    private final ProjectRepository projectRepository;
    private final PropertyTypeRepository propertyTypeRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ExcelSheetService excelSheetService;
    private final ResaleStatusRepository resaleStatusRepository;
    private final ResaleTypeRepository resaleTypeRepository;


    @Override
    public ResponseEntity<?> getResales(ResaleCriteria resaleCriteria, Transition transition) {
        if (transition.getRole().equals("USER")) {
            Employee employee =
                    employeeRepository.findById(transition.getUserId())
                            .orElseThrow(NotFoundResourceException::new);
            resaleCriteria.setSubordinates(employee.getSubordinates().stream().map(User::getId).toList());
            log.info("********************** %%%%%%%%%%%%%%%%% **********************");
            log.info(resaleCriteria.getSubordinates().toString());
        }
        Specification<Resale> leadSpecification = filter(resaleCriteria, transition);
        Pageable pageable = PageRequest.of(resaleCriteria.getPage(), resaleCriteria.getSize());
        Page<Resale> resalesPage = resaleRepository.findAll(leadSpecification, pageable);
        Page<ResaleResponse> resaleResponses = resaleMapper.toDTOPage(resalesPage);
        return success(resaleResponses);
    }

    @Override
    public ResponseEntity<?> getResaleDetails(long resaleId, Transition transition) {
        Resale resale = resaleRepository.findById(resaleId)
                .orElseThrow(NotFoundResourceException::new);
        ResaleResponse resaleResponse = resaleMapper.toDTO(resale);
        return success(resaleResponse);
    }

    @Override
    public ResponseEntity<?> addResale(AddResaleDTO resaleDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Admin admin = null;
        Employee salesRep = null;
        boolean isAdmin = transition.getRole().equals("ADMIN");

        Resale.ResaleBuilder resaleBuilder = Resale.builder()
                .name(resaleDTO.getName())
                .status(resaleStatusRepository.getReferenceById(resaleDTO.getStatus()))
                .type(resaleTypeRepository.getReferenceById(resaleDTO.getType()))
                .note(resaleDTO.getNote())
                .email(resaleDTO.getEmail())
                .phone(resaleDTO.getPhone())
                .phase(resaleDTO.getPhase())
                .code(resaleDTO.getCode())
                .creator(creator)
                .budget(resaleDTO.getBudget())
                .BUA(resaleDTO.getBUA());
        if (isAdmin) {
            resaleBuilder.admin((Admin) creator);
            if (resaleDTO.getSalesRep() != null) {
                salesRep = employeeRepository.findById(resaleDTO.getSalesRep())
                        .orElseThrow(NotFoundResourceException::new);
            }

        } else {
            salesRep = (Employee) creator;
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
            resaleBuilder.admin(admin);
        }
        resaleBuilder.salesRep(salesRep);
        if (resaleDTO.getCategory() != null) {
            resaleBuilder.category(categoryRepository.getReferenceById(resaleDTO.getCategory()));
        }

        if (resaleDTO.getProject() != null) {
            resaleBuilder.project(projectRepository.getReferenceById(resaleDTO.getProject()));
        }

        if (resaleDTO.getProperty() != null) {
            resaleBuilder.property(propertyTypeRepository.getReferenceById(resaleDTO.getProperty()));
        }
        Resale resale = resaleBuilder.build();
        Resale savedResale = resaleRepository.save(resale);
        ResaleResponse resaleResponse = resaleMapper.toDTO(savedResale);
        return success(resaleResponse);
    }

    @Override
    public ResponseEntity<?> updateResale(AddResaleDTO resaleDTO, Transition transition) {
        Resale resale = resaleRepository.findById(resaleDTO.getId())
                .orElseThrow(NotFoundResourceException::new);
        resale.setEmail(resaleDTO.getEmail());
        resale.setCode(resale.getCode());
        resale.setBudget(resale.getBudget());
        resale.setUpdatedAt(LocalDateTime.now());
        resale.setNote(resaleDTO.getNote());
        resale.setName(resaleDTO.getName());
        resale.setPhase(resaleDTO.getPhase());

        if (resaleDTO.getSalesRep() != null) {
            resale.setSalesRep(employeeRepository.getReferenceById(resaleDTO.getSalesRep()));
        }
        if (resaleDTO.getCategory() != null) {
            resale.setCategory(categoryRepository.getReferenceById(resaleDTO.getCategory()));
        }

        if (resaleDTO.getProject() != null) {
            resale.setProject(projectRepository.getReferenceById(resaleDTO.getProject()));
        }

        if (resaleDTO.getProperty() != null) {
            resale.setProperty(propertyTypeRepository.getReferenceById(resaleDTO.getProperty()));
        }

        if (resaleDTO.getType() != null) {
            resale.setType(resaleTypeRepository.getReferenceById(resaleDTO.getType()));
        }

        if (resaleDTO.getStatus() != null) {
            resale.setStatus(resaleStatusRepository.getReferenceById(resaleDTO.getStatus()));
        }

        resaleRepository.save(resale);
        ResaleResponse resaleResponse = resaleMapper.toDTO(resale);
        return success(resaleResponse);
    }

    @Override
    public ResponseEntity<?> deleteResale(Long resaleId, Transition transition) {
        resaleRepository.deleteResale(resaleId);
        return success("Resale deleted successfully");
    }

    @Override
    public ResponseEntity<?> restoreResale(Long resaleId, Transition transition) {
        resaleRepository.restoreResale(resaleId);
        return success("Resale restored successfully");
    }

    @Override
    public ResponseEntity<?> generateExcel(Transition transition) {
        ExcelFile excelFile = ExcelFile.builder()
                .header(generateHeader(AddResaleDTO.class))
                .dropdowns(excelSheetService.generateResaleSheetMap(transition))
                .build();
        return success(excelFile);
    }

    @Override
    public ResponseEntity<?> importResale(List<ImportResaleDTO> resales, Transition transition) {
        log.info("Resale List {}", resales);

        List<Resale> resaleList = createResaleList(resales, transition);
        resaleRepository.saveAll(resaleList);
        return success("Resale Imported Successfully");
    }

    @Override
    public ResponseEntity<?> isPhoneExist(List<String> phones, Transition transition) {
        HashMap<String, Object> responseBody = new HashMap<>();
        List<String> existingPhones = new ArrayList<>();

        for (String phone : phones) {
            boolean exists = resaleRepository.existsByPhone(phone);
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
        boolean isExists = resaleRepository.existsByPhone(phone);
        HashMap<String, Boolean> body = new HashMap<>();
        body.put("isExists", isExists);
        return success(body);
    }

    @Override
    public ResponseEntity<?> assignSalesToLead(AssignDTO assignDTO, Transition transition) {
        Resale resale = resaleRepository.findById(assignDTO.getLeadId())
                .orElseThrow(NotFoundResourceException::new);
        log.info("resale Id ***************");
        log.info("{} =======", resale);
        if (resale.getSalesRep() != null &&
                Objects.equals(resale.getSalesRep().getId(), assignDTO.getSalesId())) {
            return badRequest();
        }

        Employee employee = employeeRepository.findById(assignDTO.getSalesId())
                .orElseThrow(NotFoundResourceException::new);
        resale.setSalesRep(employee);
        resale.setAssignAt(LocalDateTime.now());
        // actionServiceImp.setAssignAction(lead, transition);
        AssignResponse response = AssignResponse.builder()
                .salesName(employee.getName())
                .jobTitle(employee.getJobName())
                .assignAt(LocalDateTime.now())
                .build();
        return success(response);
    }

    private List<Resale> createResaleList(List<ImportResaleDTO> importResaleDTOS, Transition transition) {
        List<Resale> resales = new ArrayList<>();
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Admin admin;
        Employee salesRep;
        boolean isAdmin = transition.getRole().equals("ADMIN");
        if (!isAdmin) {
            salesRep = employeeRepository.getReferenceById(transition.getUserId());
            admin = salesRep.getAdmin();
        } else {
            salesRep = null;
            admin = (Admin) creator;
        }

        Admin finalAdmin = admin;

        importResaleDTOS.forEach(resaleDTO -> {
            ResaleStatus resaleStatus = resaleStatusRepository.findByName(resaleDTO.getStatus());
            log.info("status {}", resaleStatus);
            Resale.ResaleBuilder resaleBuilder = Resale.builder()
                    .name(resaleDTO.getName())
                    .status(resaleStatus)
                    .type(resaleTypeRepository.findByName(resaleDTO.getType()))
                    .note(resaleDTO.getNote())
                    .email(resaleDTO.getEmail())
                    .phone(resaleDTO.getPhone())
                    .phase(resaleDTO.getPhase())
                    .code(resaleDTO.getCode())
                    .BUA(resaleDTO.getBUA())
                    .creator(creator)
                    .admin(finalAdmin)
                    .deleted(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now());
            if (!isAdmin) {
                resaleBuilder.salesRep(salesRep);
            } else if (resaleDTO.getSalesRep() != null) {
                resaleBuilder.salesRep(employeeRepository.getReferenceById(resaleDTO.getSalesRep()));
            }
            if (resaleDTO.getProject() != null) {
                resaleBuilder.project(projectRepository.findByNameAndAdminId(resaleDTO.getProject(), finalAdmin.getId()));
            }

            if (resaleDTO.getCategory() != null) {
                resaleBuilder.category(categoryRepository.findByNameAndAdminId(resaleDTO.getCategory(), finalAdmin.getId()));
            }
            Resale resale = resaleBuilder.build();

            resales.add(resale);
        });

        return resales;
    }
}
