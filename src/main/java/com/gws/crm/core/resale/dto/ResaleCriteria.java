package com.gws.crm.core.resale.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResaleCriteria {
    private int id;
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
    private long creator;
    private LocalDate createdAt;
    private int page;
    private int size;
}
