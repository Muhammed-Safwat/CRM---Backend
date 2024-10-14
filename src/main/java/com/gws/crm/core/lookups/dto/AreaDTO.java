package com.gws.crm.core.lookups.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AreaDTO extends LockupDTO{

    @NotNull
    private LockupDTO region;
}
