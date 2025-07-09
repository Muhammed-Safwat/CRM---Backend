package com.gws.crm.authentication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PrivilegeGroupRes {
    private long id ;
    private String name ;
    private String employeeCount ;
    private boolean status;
}
