package com.gws.crm.core.lookups.dto;

import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.entity.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
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
