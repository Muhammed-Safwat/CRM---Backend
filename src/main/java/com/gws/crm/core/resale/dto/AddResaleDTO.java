package com.gws.crm.core.resale.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddResaleDTO {
    private String name;
    private String phone;
    private String email;
    private long project;
    private String BUA;
    private String Phase;
    private String Code;
    private long category;
    private long property;
    private String note;
}
