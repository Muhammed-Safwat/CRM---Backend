package com.gws.crm.core.leads.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportPreLeadDTO {
    private String name;
    private String country;
    private String phoneNumbers;
    private String channel;
    private String category;
    private String link;
    private String note;
    private String project;
}
