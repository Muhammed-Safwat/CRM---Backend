package com.gws.crm.core.leads.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class SalesLeadCriteria {
    private String keyword;
    private Long id;
    private Long status;
    private List<Long> investmentGoal;
    private List<Long> communicateWay;
    private List<Long> cancelReasons;
    private List<Long> salesRep;
    private List<Long> channel;
    private List<Long> broker;
    private boolean deleted;
    private boolean myLead;
    private List<Long> project;
    private String country;
    private Long creator;
    private String campaignId;
    private LocalDate lastActionDate;
    private LocalDate lastActionNoAction;
    private LocalDate stageDate;
    private LocalDate actionDate;
    private LocalDate assignDate;
    private String budget;
    private String hasPayment;
    private String noAnswers;
    private LocalDate createdAt;
    private Long type;
    private int page;
    private int size;
}
