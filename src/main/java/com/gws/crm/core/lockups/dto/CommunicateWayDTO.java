package com.gws.crm.core.lockups.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommunicateWayDTO {

    @NotEmpty
    @NotNull
    private String name;
    private long id;

}
