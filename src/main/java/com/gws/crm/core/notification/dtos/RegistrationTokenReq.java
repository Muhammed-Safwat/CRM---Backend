package com.gws.crm.core.notification.dtos;


import com.gws.crm.core.notification.enums.ClientType;
import lombok.Getter;

@Getter
public class RegistrationTokenReq {
    private  String token ;
    private String clientType ;
}
