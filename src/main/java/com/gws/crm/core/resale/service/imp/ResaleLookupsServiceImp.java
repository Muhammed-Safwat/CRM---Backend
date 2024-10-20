package com.gws.crm.core.resale.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.entity.Project;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import com.gws.crm.core.lookups.repository.ProjectRepository;
import com.gws.crm.core.resale.dto.ResaleLookupsDTO;
import com.gws.crm.core.resale.repository.ResaleStatusRepository;
import com.gws.crm.core.resale.repository.ResaleTypeRepository;
import com.gws.crm.core.resale.service.ResaleLookupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class ResaleLookupsServiceImp implements ResaleLookupsService {

    private final CategoryRepository categoryRepository;
    private final ResaleTypeRepository resaleTypeRepository;
    private final ResaleStatusRepository resaleStatusRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ResponseEntity<?> getLookups(Transition transition) {
        List<Project> projects = projectRepository.findAllByAdminId(transition.getUserId());

        ResaleLookupsDTO resaleLookupsDTO = ResaleLookupsDTO.builder()
                .categories(categoryRepository.findAllByAdminId(transition.getUserId()))
                .types(resaleTypeRepository.findAll())
                .statuses(resaleStatusRepository.findAll())
                .projects(projects)
                .build();
        return success(resaleLookupsDTO);
    }
}