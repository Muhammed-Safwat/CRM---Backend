package com.gws.crm.core.leads.dto;

import lombok.Getter;

@Getter
public class LeadCriteria {
    private String keyword;
    private long id;
    private long status;
    private long investmentGoal;
    private long communicateWay;
    private long cancelReasons;
    private long salesRep;
    private long channel;
    private long project;
    private boolean deleted;
    private long creator;
    private int page;
    private int size;
}
