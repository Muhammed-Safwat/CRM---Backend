package com.gws.crm.core.leads.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LeadStatusDto {
    private long id;
    private String name;
}
