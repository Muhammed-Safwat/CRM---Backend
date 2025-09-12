package com.gws.crm.core.lookups.mapper;

import com.gws.crm.core.lookups.dto.ProjectDTO;
import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.entity.Project;
import com.gws.crm.core.lookups.entity.Region;

import java.util.List;

public class ProjectMapper {

    public static ProjectDTO toDTO(Project project) {
        if (project == null) return null;

        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .region(project.getRegion() != null ? project.getRegion().getName() : null)
                .category(project.getCategory() != null ? project.getCategory().getName() : null)
                .devCompany(project.getDevCompany() != null ? project.getDevCompany().getName() : null)
                .build();
    }

    public static List<ProjectDTO> toDTO(List<Project> projects){
        if(projects == null) return  null;
        return projects.stream().map(ProjectMapper::toDTO).toList();
    }

    public static Project toEntity(ProjectDTO dto, Region region, Category category, DevCompany devCompany) {
        if (dto == null) return null;

        return Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .region(region)
                .category(category)
                .devCompany(devCompany)
                .build();
    }

    public static void updateEntity(Project project, ProjectDTO dto, Region region, Category category, DevCompany devCompany) {
        if (dto == null || project == null) return;

        project.setName(dto.getName());
        project.setRegion(region);
        project.setCategory(category);
        project.setDevCompany(devCompany);
    }
}
