package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.employee.entity.Admin;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.ProjectDTO;
import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.entity.Project;
import com.gws.crm.core.lookups.entity.Region;
import com.gws.crm.core.lookups.mapper.ProjectMapper;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.lookups.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Log
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final RegionRepository regionRepository;
    private final DevCompanyRepository devCompanyRepository;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final AdminRepository adminRepository;
    private final LookupRepositoryUtils lookupRepositoryUtils;


    @Override
    public ResponseEntity<?> getProjects(int page, int size, String keyword, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Project> projectPage = projectRepository.findAllByAdminIdAndKeyword(pageable, transition.getUserId(),
                keyword);
        Page<ProjectDTO> projectDTOs = projectPage.map(ProjectMapper::toDTO);
        return success(projectDTOs);
    }

    @Override
    public ResponseEntity<?> getAllProjects(Transition transition) {
        List<Project> projects = projectRepository.findAllByAdminId(transition.getUserId());
        List<ProjectDTO> projectDTOS = ProjectMapper.toDTO(projects);
        return success(projectDTOS);
    }

    @Override
    public ResponseEntity<?> getProjectById(long id, Transition transition) {
        Project project = projectRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        ProjectDTO projectDTO = ProjectMapper.toDTO(project);
        return success(projectDTO);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createProject(ProjectDTO projectDTO, Transition transition) {
        Admin admin = adminRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Project project = Project.builder()
                .admin(admin)
                .name(projectDTO.getName())
                .region(resolveRegion(projectDTO.getRegion(),admin))
                .category(resolveCategory(projectDTO.getCategory(),admin))
                .devCompany(resolveDevCompany(projectDTO.getDevCompany(),admin))
                .deleted(false)
                .build();

        Project savedProject = projectRepository.save(project);
        log.info(savedProject.toString());
        return success(ProjectMapper.toDTO(savedProject));
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateProject(ProjectDTO projectDTO, Transition transition) {
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(NotFoundResourceException::new);

        project.setName(projectDTO.getName());
        project.setRegion(regionRepository.findByNameAndAdminId(projectDTO.getRegion(),transition.getUserId()).orElseThrow(NotFoundResourceException::new));
        project.setCategory(categoryRepository.findByNameAndAdminId(projectDTO.getCategory(),transition.getUserId()).orElseThrow(NotFoundResourceException::new));
        project.setDevCompany(devCompanyRepository.findByNameAndAdminId(projectDTO.getDevCompany(),transition.getUserId()).orElseThrow(NotFoundResourceException::new));

        Project updatedProject = projectRepository.save(project);
        ProjectDTO dto = ProjectMapper.toDTO(updatedProject);
        return success(dto);
    }



    private Region resolveRegion(String name,Admin admin) {
        return LookupRepositoryUtils.getOrCreateByAttribute(
                regionRepository,
                name,
                regionRepository::getByName,
                () -> {
                    Region r = new Region();
                    r.setName(name);
                    r.setAdmin(admin);
                    return r;
                }
        );
    }

    private Category resolveCategory(String name,Admin admin) {
        return LookupRepositoryUtils.getOrCreateByAttribute(
                categoryRepository,
                name,
                categoryRepository::getByName,
                () -> {
                    Category c = new Category();
                    c.setName(name);
                    c.setAdmin(admin);
                    return c;
                }
        );
    }

    private DevCompany resolveDevCompany(String name,Admin admin) {
        return LookupRepositoryUtils.getOrCreateByAttribute(
                devCompanyRepository,
                name,
                devCompanyRepository::getByName,
                () -> {
                    DevCompany d = new DevCompany();
                    d.setName(name);
                    d.setAdmin(admin);
                    return d;
                }
        );
    }

    @Override
    public ResponseEntity<?> deleteProject(long id, Transition transition) {
        projectRepository.deleteProject(id);
        return success("Project deleted successfully");
    }
}
