package com.gws.crm.core.employee.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserActivityReq {

    long userId;
    int page;
    int size;
}
