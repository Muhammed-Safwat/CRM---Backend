package com.gws.crm.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class AdminRegistrationDto {
    private long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "Username is required")
    @Email(message = "Email should be valid")
    private String username;

    private String password;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotNull(message = "Account Expiration Date Should not be null")
    private LocalDateTime accountExpirationDate;

    @NotNull(message = "ANumber of allowed users Date Should not be null")
    @Min(value = 1, message = "Number of users should be greater than 1")
    private int numberOfUsers;

    // ====== Company Info ======
    private String companyAddress;

    private String companyDescription;

    @Email(message = "Company email should be valid")
    private String companyEmail;

    private String companyLogoUrl;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String companyPhone;

    private String companyWebsite;
}
