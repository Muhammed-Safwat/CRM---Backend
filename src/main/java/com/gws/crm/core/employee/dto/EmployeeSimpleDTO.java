package com.gws.crm.core.employee.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmployeeSimpleDTO {

    private long id;

    private String name;

    private String jobName;
}
