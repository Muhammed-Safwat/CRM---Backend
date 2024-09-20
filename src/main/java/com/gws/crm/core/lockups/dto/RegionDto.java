package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegionDto {

    @NotNull(message = "name is required")
    @NotBlank(message = "name is required")
    private String name;
    private long id;
}
