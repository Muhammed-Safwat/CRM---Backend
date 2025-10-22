package com.gws.crm.core.leads.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EngazLeadDto {
    private String fullName;
    private Integer countryCode;
    private String mobile;
    private String region;
    private String project;
    private String salesRep;
    private String assignDate;
    private String channel;
    private String lastComment;
    private List<EngazActionDto> actions;
}
