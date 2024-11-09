package com.gws.crm.core.employee.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmployeeCriteria {
    private List<String> jobName;
    private String status;
    private String keyword;
    private List<LocalDateTime> createdAt;
    private boolean deleted ;
    private int page;
    private int size;
}
