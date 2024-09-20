package com.gws.crm.authentication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
}
