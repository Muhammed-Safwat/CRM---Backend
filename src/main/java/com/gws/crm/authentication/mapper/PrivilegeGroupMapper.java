package com.gws.crm.authentication.mapper;

import com.gws.crm.authentication.dto.PrivilegeGroupDetailsRes;
import com.gws.crm.authentication.entity.PrivilegeGroup;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PrivilegeGroupMapper {

    public PrivilegeGroupDetailsRes toDto(PrivilegeGroup privilegeGroup, List<EmployeeSimpleDTO> employees) {
        return PrivilegeGroupDetailsRes.builder()
                .id(privilegeGroup.getId())
                .name(privilegeGroup.getJobName())
                .employeeCount(employees.size())
                .status(privilegeGroup.isStatus())
                .description(privilegeGroup.getDescription())
                .employees(employees)
                .privileges(privilegeGroup.getPrivileges())
                .build();
    }

}
