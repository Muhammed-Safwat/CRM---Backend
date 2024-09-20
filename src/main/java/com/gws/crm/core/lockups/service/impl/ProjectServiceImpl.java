package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.lockups.dto.ProjectDTO;
import com.gws.crm.core.lockups.entity.Project;
import com.gws.crm.core.lockups.repository.CategoryRepository;
import com.gws.crm.core.lockups.repository.DevCompanyRepository;
import com.gws.crm.core.lockups.repository.ProjectRepository;
import com.gws.crm.core.lockups.repository.RegionRepository;
import com.gws.crm.core.lockups.service.ProjectService;
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

    @Override
    public ResponseEntity<?> getProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Project> projectPage = projectRepository.findAll(pageable);
        return success(projectPage);
    }

    @Override
    public ResponseEntity<ApiResponse<List<Project>>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return success(projects);
    }

    @Override
    public ResponseEntity<ApiResponse<Project>> getProjectById(long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        return success(project);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createProject(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .projectName(projectDTO.getProjectName())
                .region(regionRepository.findById(projectDTO.getRegion().getId()).get())
                .category(categoryRepository.findById(projectDTO.getCategory().getId()).get())
                .devCompany(devCompanyRepository.findById(projectDTO.getDevCompany().getId()).get())
                .build();

        Project savedProject = projectRepository.save(project);
        log.info(savedProject.toString());
        return success(savedProject);
    }

    @Override
    public ResponseEntity<ApiResponse<Project>> updateProject(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(NotFoundResourceException::new);

        project.setProjectName(projectDTO.getProjectName());
        project.setRegion(regionRepository.findById(projectDTO.getRegion().getId()).get());
        project.setCategory(categoryRepository.findById(projectDTO.getCategory().getId()).get());
        project.setDevCompany(devCompanyRepository.findById(projectDTO.getDevCompany().getId()).get());

        projectRepository.save(project);
        return success(project);
    }

    @Override
    public ResponseEntity<?> deleteProject(long id) {
        projectRepository.deleteById(id);
        return success("Project deleted successfully");
    }
}
