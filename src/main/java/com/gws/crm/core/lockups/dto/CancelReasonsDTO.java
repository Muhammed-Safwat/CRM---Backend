package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelReasonsDTO {
    private long id;

    @NotNull(message = "Cancel reason is required")
    private String name;
}
