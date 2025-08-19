package com.gws.crm.core.employee.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AdminBasicsInfo {

    private long id;

    private String name;

    private String username;

    private String image;

    private String phone;

    private boolean locked;

    private boolean enabled;

    private boolean deleted;

    private LocalDateTime accountNonExpired;

    private LocalDateTime credentialsNonExpired;

    private int maxNumberOfUsers;

    private CompanyDTO companyDTO;
}
