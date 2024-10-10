package com.gws.crm.core.owners.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOwnerDto {
    private String name;
    private String phone;
    private String email;
    private long projects;
    private String BUA;
    private String Phase;
    private String Code;
    private String category;
    private String note;
}
