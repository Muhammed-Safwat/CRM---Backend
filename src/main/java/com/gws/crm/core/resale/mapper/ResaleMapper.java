package com.gws.crm.core.resale.mapper;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.dto.ProjectDTO;
import com.gws.crm.core.resale.dto.ResaleResponse;
import com.gws.crm.core.resale.entities.Resale;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResaleMapper {

    public ResaleResponse toSimpleDTO(Resale resale) {
        if (resale == null) {
            return null;
        }


        LookupDTO statusDTO = resale.getStatus() != null ?
                LookupDTO.builder()
                        .id(resale.getStatus().getId())
                        .name(resale.getStatus().getName())
                        .build() :
                null;

        /*ProjectDTO projectDTO = resale.getProject() != null ?
                ProjectDTO.builder()
                        .id(resale.getProject().getId())
                        .name(resale.getProject().getName())
                        .build()
                : null;

         */

        EmployeeSimpleDTO sealRep = resale.getSalesRep() != null ?
                EmployeeSimpleDTO.builder()
                        .id(resale.getSalesRep().getId())
                        .name(resale.getSalesRep().getName())
                        .build()
                : null;


        return ResaleResponse.builder()
                .id(resale.getId())
                .phone(resale.getPhone())
                .name(resale.getName())
               // .project(projectDTO)
                .status(statusDTO)
                .salesRep(sealRep)
                .deleted(resale.isDeleted())
                .assignAt(resale.getAssignAt())
                .delayed(resale.isDelay())
                .build();
    }

    public List<ResaleResponse> toSimpleDTOList(List<Resale> leads) {
        return leads.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<ResaleResponse> toSimpleDTOPage(Page<Resale> leadPage) {
        return leadPage.map(this::toDTO);
    }

    public ResaleResponse toDTO(Resale resale) {
        if (resale == null) {
            return null;
        }

        LookupDTO categoryDTO = resale.getCategory() != null ?
                LookupDTO.builder()
                        .id(resale.getCategory().getId())
                        .name(resale.getCategory().getName())
                        .build() :
                null;
        LookupDTO statusDTO = resale.getStatus() != null ?
                LookupDTO.builder()
                        .id(resale.getStatus().getId())
                        .name(resale.getStatus().getName())
                        .build() :
                null;
        LookupDTO typeDTO = resale.getType() != null ?
                LookupDTO.builder()
                        .id(resale.getType().getId())
                        .name(resale.getType().getName())
                        .build() :
                null;

        LookupDTO propertyDTO = resale.getProperty() != null ?
                LookupDTO.builder()
                        .id(resale.getProperty().getId())
                        .name(resale.getProperty().getName())
                        .build() :
                null;

        ProjectDTO projectDTO = resale.getProject() != null ?
                ProjectDTO.builder()
                        .id(resale.getProject().getId())
                        .name(resale.getProject().getName())
                        .build()
                : null;

        EmployeeSimpleDTO creatorDTO = resale.getCreator() != null ?
                EmployeeSimpleDTO.builder()
                        .id(resale.getCreator().getId())
                        .name(resale.getCreator().getName())
                        .build()
                : null;
        EmployeeSimpleDTO sealRep = resale.getSalesRep() != null ?
                EmployeeSimpleDTO.builder()
                        .id(resale.getSalesRep().getId())
                        .name(resale.getSalesRep().getName())
                        .build()
                : null;


        return ResaleResponse.builder()
                .id(resale.getId())
                .phone(resale.getPhone())
                .name(resale.getName())
                .email(resale.getEmail())
                .updatedAt(resale.getUpdatedAt())
                .createdAt(resale.getCreatedAt())
                .note(resale.getNote())
                .project(projectDTO)
                .category(categoryDTO)
                .property(propertyDTO)
                .status(statusDTO)
                .type(typeDTO)
                .salesRep(sealRep)
                .deleted(resale.isDeleted())
                .creator(creatorDTO)
                .assignAt(resale.getAssignAt())
                .delayed(resale.isDelay())
                .build();
    }

    public List<ResaleResponse> toDTOList(List<Resale> leads) {
        return leads.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<ResaleResponse> toDTOPage(Page<Resale> leadPage) {
        return leadPage.map(this::toDTO);
    }
}
