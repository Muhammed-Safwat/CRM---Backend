package com.gws.crm.core.lookups.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDTO {

    private long id;

    @NotNull
    @NotBlank
    private String name;

    /*
        @NotNull
        private Region region;

        @NotNull
        private Category category;

        @NotNull
        private DevCompany devCompany;
    */

    private String region;

    private String category;

    private String devCompany;
}
