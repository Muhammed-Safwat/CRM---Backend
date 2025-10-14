package com.gws.crm.core.leads.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberDTO {
    @NotEmpty
    @NotNull
    private String phone;
}
