package com.gws.crm.core.lockups.service;

import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.lockups.dto.ProjectDTO;
import com.gws.crm.core.lockups.entity.Project;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {

    ResponseEntity<?> getProjects(int page, int size);

    ResponseEntity<ApiResponse<List<Project>>> getAllProjects();

    ResponseEntity<ApiResponse<Project>> getProjectById(long id);

    ResponseEntity<?> createProject(ProjectDTO projectDTO);

    ResponseEntity<ApiResponse<Project>> updateProject(ProjectDTO projectDTO);

    ResponseEntity<?> deleteProject(long id);
}
