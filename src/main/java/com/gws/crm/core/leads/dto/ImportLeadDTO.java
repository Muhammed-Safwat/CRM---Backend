package com.gws.crm.core.leads.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportLeadDTO {
    private String name;
    private String country;
    private String phoneNumbers;
    private String cancelReason;
    private String channel;
    private String communicateWay;
    private String contactTime;
    private String email;
    private String investmentGoal;
    private String jobTitle;
    private String note;
    private String whatsappNumber;
    private String project;
    private String salesRep;
    private String status;
    private String budget;
}
