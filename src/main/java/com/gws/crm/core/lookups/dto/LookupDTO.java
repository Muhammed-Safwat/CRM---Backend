package com.gws.crm.core.lookups.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LookupDTO {

    private long id;

    @NotNull(message = "Name is required")
    private String name;
}
