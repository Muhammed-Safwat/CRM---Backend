package com.gws.crm.core.resale.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResaleDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String status;

    @NotNull
    @NotBlank
    private String type;

    @NotNull
    @NotBlank
    private String phone;

    private String email;

    private String project;

    private String BUA;

    private String Phase;

    private String Code;

    private String category;

    private String property;

    private String note;

    private Long salesRep;
}
