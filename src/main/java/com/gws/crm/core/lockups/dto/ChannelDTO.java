package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChannelDTO {
    private long id;

    @NotNull
    @NotBlank
    private String name;
}
