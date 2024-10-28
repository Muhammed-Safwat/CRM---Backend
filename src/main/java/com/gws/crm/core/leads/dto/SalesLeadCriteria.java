package com.gws.crm.core.leads.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private List<Long> creator;
    private String campaignId;
    private List<LocalDateTime> lastActionDate;
    private List<Long> lastAction;
    private List<Long> stage;
    private List<LocalDateTime> stageDate;
    private List<LocalDateTime> actionDate;
    private List<LocalDateTime> assignDate;
    private String budget;
    private String hasPayment;
    private String noAnswers;
    private List<LocalDateTime> createdAt;
    private Long type;
    private int page;
    private int size;
}
