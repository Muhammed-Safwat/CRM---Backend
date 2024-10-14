package com.gws.crm.core.lookups.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LockupDTO {

    private long id;

    @NotNull(message = "Name is required")
    private String name;
}
