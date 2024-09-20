package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StageDto {
    private long id;
    @NotNull
    @NotEmpty
    private String name;

}

