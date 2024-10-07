package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CommunicateWayDTO {

    @NotEmpty
    @NotNull
    private String name;
    private long id;

}
