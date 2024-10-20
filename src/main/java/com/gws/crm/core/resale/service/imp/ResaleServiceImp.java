package com.gws.crm.core.resale.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.repository.EmployeeRepository;
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
import java.util.List;

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
        Specification<Resale> leadSpecification = filter(resaleCriteria, transition);
        Pageable pageable = PageRequest.of(resaleCriteria.getPage(), resaleCriteria.getSize());
        Page<Resale> resalesPage = resaleRepository.findAll(leadSpecification, pageable);
        Page<ResaleResponse> resaleResponses = resaleMapper.toDTOPage(resalesPage);
        return success(resaleResponses);
    }

    @Override
    public ResponseEntity<?> getResaleDetails(long resaleId, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> addResale(AddResaleDTO resaleDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        Admin admin = null;

        Resale.ResaleBuilder resaleBuilder = Resale.builder()
                .name(resaleDTO.getName())
                .status(resaleStatusRepository.getReferenceById(resaleDTO.getStatus()))
                .type(resaleTypeRepository.getReferenceById(resaleDTO.getType()))
                .salesRep(employeeRepository.getReferenceById(resaleDTO.getSalesRep()))
                .note(resaleDTO.getNote())
                .email(resaleDTO.getEmail())
                .phone(resaleDTO.getPhone())
                .phase(resaleDTO.getPhase())
                .code(resaleDTO.getCode())
                .creator(creator)
                .budget(resaleDTO.getBudget())
                .BUA(resaleDTO.getBUA());

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
            resaleBuilder.admin(admin);
        } else {
            resaleBuilder.admin((Admin) creator);
        }

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
        return null;
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
        log.info("Resale List {}",resales);

        List<Resale> resaleList = createResaleList(resales, transition);
        resaleRepository.saveAll(resaleList);
        return success("Resale Imported Successfully");
    }

    private List<Resale> createResaleList(List<ImportResaleDTO> importResaleDTOS, Transition transition) {
        List<Resale> resales = new ArrayList<>();
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Admin admin;

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
        } else {
            admin = (Admin) creator;
        }

        Admin finalAdmin = admin;

        importResaleDTOS.forEach(resaleDTO -> {
            ResaleStatus resaleStatus = resaleStatusRepository.findByName(resaleDTO.getStatus());
            log.info("status {}",resaleStatus);
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
