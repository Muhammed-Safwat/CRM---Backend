package com.gws.crm.core.lockups.dto;

import com.gws.crm.core.lockups.entity.Category;
import com.gws.crm.core.lockups.entity.DevCompany;
import com.gws.crm.core.lockups.entity.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProjectDTO {

    private long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Region region;

    @NotNull
    private Category category;

    @NotNull
    private DevCompany devCompany;

}
