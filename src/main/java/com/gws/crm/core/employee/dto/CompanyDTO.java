package com.gws.crm.core.employee.dto;

import lombok.Data;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private String logoUrl;
    private String email;
    private String phone;
    private String website;
    private String address;
    private String description;
}
