package com.gws.crm.authentication.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PrivilegeGroupCriteria {

    private int page;

    private int size;

    private String keyword;

    private Boolean status;

}
