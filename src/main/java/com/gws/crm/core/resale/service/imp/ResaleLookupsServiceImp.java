package com.gws.crm.core.resale.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.lookups.dto.SimpleProjectDto;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import com.gws.crm.core.lookups.repository.ProjectRepository;
import com.gws.crm.core.resale.dto.ResaleLookupsDTO;
import com.gws.crm.core.resale.repository.ResaleStatusRepository;
import com.gws.crm.core.resale.repository.ResaleTypeRepository;
import com.gws.crm.core.resale.service.ResaleLookupsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResaleLookupsServiceImp implements ResaleLookupsService {
    private final EmployeeRepository employeeRepository;
    private final CategoryRepository categoryRepository;
    private final ResaleTypeRepository resaleTypeRepository;
    private final ResaleStatusRepository resaleStatusRepository;
    private final ProjectRepository projectRepository;


    @Override
    public ResponseEntity<?> getLookups(Transition transition) {
        long id = transition.getUserId();
        if (!transition.getRole().equals("ADMIN")) {
            id = employeeRepository.findAdminId(transition.getUserId());
        }
        List<SimpleProjectDto> projects = projectRepository.findSimpleProjectData(id);
        List<EmployeeSimpleDTO> salesReps = employeeRepository.findSimpleEmployeeByAdminId(id);

        ResaleLookupsDTO resaleLookupsDTO = ResaleLookupsDTO.builder()
                .categories(categoryRepository.findAllByAdminIdAndDeletedFalse(id))
                .types(resaleTypeRepository.findAll())
                .statuses(resaleStatusRepository.findAll())
                .projects(projects)
                .employees(salesReps)
                .build();
        return success(resaleLookupsDTO);
    }
}