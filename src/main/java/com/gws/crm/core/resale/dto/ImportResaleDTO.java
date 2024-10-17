package com.gws.crm.core.resale.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResaleDTO {
    private String name;
    private String phone;
    private String email;
    private String project;
    private String BUA;
    private String Phase;
    private String Code;
    private String category;
    private String property;
    private String note;
}
