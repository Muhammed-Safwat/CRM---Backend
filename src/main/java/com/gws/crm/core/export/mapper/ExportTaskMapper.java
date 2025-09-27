package com.gws.crm.core.export.mapper;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.export.dtos.ExportResponseDto;
import com.gws.crm.core.export.entity.ExportTask;
import org.springframework.stereotype.Service;

@Service
public class ExportTaskMapper {

    public ExportResponseDto mapToDto(ExportTask task) {
        return ExportResponseDto.builder()
                .id(task.getId())
                .filename(task.getFilename())
                .status(task.getStatus() != null ? task.getStatus().name() : null)
                .createdAt(task.getCreatedAt())
                .sizeBytes(task.getSizeBytes())
                .filePath(task.getFilePath())
                .type(task.getType())
                .exportedBy(mapUserToEmployeeSimpleDTO(task.getExportedBy()))
                .build();
    }

    private EmployeeSimpleDTO mapUserToEmployeeSimpleDTO(com.gws.crm.authentication.entity.User user) {
        if (user == null) {
            return null;
        }

        // Assuming EmployeeSimpleDTO has these fields - adjust as needed
        return EmployeeSimpleDTO.builder()
                .id(user.getId())
                .name(user.getUsername()) // or whatever field represents the name
                // Add other fields as needed based on your EmployeeSimpleDTO structure
                .build();
    }
}