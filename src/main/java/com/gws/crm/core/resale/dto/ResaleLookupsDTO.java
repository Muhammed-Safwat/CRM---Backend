package com.gws.crm.core.resale.dto;

import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.entity.Project;
import com.gws.crm.core.resale.entities.ResaleStatus;
import com.gws.crm.core.resale.entities.ResaleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
@Builder
public class ResaleLookupsDTO {
    private List<Category> categories;
    private List<Project> projects;
    private List<ResaleType> types;
    private List<ResaleStatus> statuses;
}
