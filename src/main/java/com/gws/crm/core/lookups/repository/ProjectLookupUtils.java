package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.employee.entity.Admin;
import com.gws.crm.core.lookups.entity.Project;

public class ProjectLookupUtils {

    public static Project getOrCreateProject(ProjectRepository projectRepository, String name, Admin admin) {
        return projectRepository.findByName(name, admin.getId())
                .orElseGet(() -> {
                    Project project = new Project();
                    project.setName(name);
                    project.setAdmin(admin);
                    return projectRepository.save(project);
                });
    }
}
