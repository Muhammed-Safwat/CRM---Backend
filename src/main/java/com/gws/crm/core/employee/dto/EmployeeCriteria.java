package com.gws.crm.core.employee.dto;

import lombok.Data;

@Data
public class EmployeeCriteria {
    private String jobName;
    private String enabled;
    private String keyword;
    private boolean deleted ;
    private int page;
    private int size;
}
