package com.gws.crm.core.notification.dtos;


import lombok.Getter;

@Getter
public class RegistrationTokenReq {
    private String token;
    private String clientType;
}
