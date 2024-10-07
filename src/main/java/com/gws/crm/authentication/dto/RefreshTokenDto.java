package com.gws.crm.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenDto {

    @NotNull
    @NotBlank
    private String refreshToken;

}
