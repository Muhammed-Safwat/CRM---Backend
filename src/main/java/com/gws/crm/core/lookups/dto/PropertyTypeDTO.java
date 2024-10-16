package com.gws.crm.core.lookups.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyTypeDTO extends LookupDTO{

    private LookupDTO category;
}
