package com.gws.crm.core.leads.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EngazActionDto {
    private String clientName;
    private String mobile;
    private String stage;
    private String followDate;
    private String salesRep;
    private String comment;
}
