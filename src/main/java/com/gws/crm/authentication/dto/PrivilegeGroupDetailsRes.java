package com.gws.crm.authentication.dto;

import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PrivilegeGroupDetailsRes {
    private long id;
    private String name;
    private long employeeCount;
    private boolean status;
    private String description;
    private List<EmployeeSimpleDTO> employees;
    private List<Privilege> privileges;
}
