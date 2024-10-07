package com.gws.crm.core.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EmployeeSimpleDTO {

    private long id;

    private String name;

    private String jobName;
}
