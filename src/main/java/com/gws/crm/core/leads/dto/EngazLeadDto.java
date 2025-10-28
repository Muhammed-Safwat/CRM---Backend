package com.gws.crm.core.leads.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EngazLeadDto {

    @JsonProperty("Full Name")
    private String fullName;

    @JsonProperty("Country Code")
    private Integer countryCode;

    @JsonProperty("Mobile")
    private String mobile;

    @JsonProperty("Other Phones")
    private String otherPhones;

    @JsonProperty("WhatsappNumber")
    private String whatsappNumber;

    @JsonProperty("Call Best Time")
    private String callBestTime;

    @JsonProperty("Investment Goal")
    private String investmentGoal;

    @JsonProperty("Region")
    private String region;

    @JsonProperty("Project")
    private String project;

    @JsonProperty("Last Stage")
    private String lastStage;

    @JsonProperty("Campaign id")
    private String campaignId;

    @JsonProperty("Living Country")
    private String livingCountry;

    @JsonProperty("Cancel Reason")
    private String cancelReason;

    @JsonProperty("JobTitle")
    private String jobTitle;

    @JsonProperty("Sales Rep")
    private String salesRep;

    @JsonProperty("Last Action Date")
    private String lastActionDate;

    @JsonProperty("Last Action")
    private String lastAction;

    @JsonProperty("Channel")
    private String channel;

    @JsonProperty("Stage Date")
    private String stageDate;

    @JsonProperty("ActionDate")
    private String actionDate;

    @JsonProperty("Assign Date")
    private String assignDate;

    @JsonProperty("Last Comment")
    private String lastComment;

    @JsonProperty("Budget")
    private String budget;

    @JsonProperty("Actions")
    private List<EngazActionDto> actions;
}
