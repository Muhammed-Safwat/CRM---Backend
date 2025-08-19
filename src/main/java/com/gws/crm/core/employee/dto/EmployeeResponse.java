package com.gws.crm.core.employee.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeResponse {
    private long id;

    private String name;

    private String username;

    private String image;

    private String phone;

    private boolean locked = false;

    private boolean enabled = false;

    private boolean deleted = false;

    private LocalDateTime accountNonExpired;

    private LocalDateTime credentialsNonExpired;

    private String jobName;
}
