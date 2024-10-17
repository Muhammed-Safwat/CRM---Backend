package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.ProjectDTO;
import com.gws.crm.core.lookups.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lookups/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getProjects(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size, Transition transition) {
        return projectService.getProjects(page, size, transition);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects(Transition transition) {
        return projectService.getAllProjects(transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable long id, Transition transition) {
        return projectService.getProjectById(id, transition);
    }

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO projectDTO, Transition transition) {
        return projectService.createProject(projectDTO, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateProject(@Valid @RequestBody ProjectDTO projectDTO, Transition transition) {
        return projectService.updateProject(projectDTO, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable long id, Transition transition) {
        return projectService.deleteProject(id, transition);
    }
}