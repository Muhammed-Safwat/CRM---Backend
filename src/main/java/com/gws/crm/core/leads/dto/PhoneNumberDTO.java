package com.gws.crm.core.leads.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PhoneNumberDTO {
    @NotEmpty
    @NotNull
    private String code;

    @NotEmpty
    @NotNull
    private String phone;
}
