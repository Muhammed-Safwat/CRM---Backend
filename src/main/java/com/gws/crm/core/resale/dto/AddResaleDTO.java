package com.gws.crm.core.resale.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddResaleDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String phone;

    @NotNull
    private Long status;

    @NotNull
    private Long type;

    @NotNull
    private long salesRep;

    private String email;

    private Long project;

    private String BUA;

    private String phase;

    private String code;

    private Long category;

    private Long property;

    private String note;

    private String budget;
}
