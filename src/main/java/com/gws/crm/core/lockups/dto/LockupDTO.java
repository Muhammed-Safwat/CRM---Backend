package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LockupDTO {
    private long id;

    @NotNull(message = "Name is required")
    private String name;
}
