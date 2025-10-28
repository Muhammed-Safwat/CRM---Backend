package com.gws.crm.core.lookups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.lookups.dto.ProjectDTO;
import com.gws.crm.core.lookups.entity.Project;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {

    ResponseEntity<?> getProjects(int page, int size, String keyword, Transition transition);

    ResponseEntity<?> getAllProjects(Transition transition);

    ResponseEntity<?> getProjectById(long id, Transition transition);

    ResponseEntity<?> createProject(ProjectDTO projectDTO, Transition transition);

    ResponseEntity<?> updateProject(ProjectDTO projectDTO, Transition transition);

    ResponseEntity<?> deleteProject(long id, Transition transition);

    ResponseEntity<?> createAllProjects(List<ProjectDTO> projectsList, Transition transition);
}
