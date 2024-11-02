package com.gws.crm.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ProfileSettingDTO {
    private  String name ;
    private  String email ;
    private  String phoneNumber;
    private LocalDateTime expirationDate;
    private Long maxUsers;
    private Long numEmployees;
    private String status;
    private Long id;
}
