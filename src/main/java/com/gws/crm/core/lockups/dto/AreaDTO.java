package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AreaDTO {
    @NotNull
    @NotEmpty
    private String name;
    private long id;
    @NotNull
    private RegionDto region;
}
