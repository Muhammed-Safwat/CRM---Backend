package com.gws.crm.core.employee.dto;

import com.gws.crm.authentication.entity.Privilege;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class EmployeeInfoResponse {

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

    private Set<Privilege> privileges;

    private List<EmployeeSimpleDTO> subordinates;

}
