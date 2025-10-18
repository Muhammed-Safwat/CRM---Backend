package com.gws.crm.core.lookups.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SimpleProjectDto {

    private long id;

    @NotNull
    @NotBlank
    private String name;
}
