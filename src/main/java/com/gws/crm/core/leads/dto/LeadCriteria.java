package com.gws.crm.core.leads.dto;

import lombok.Getter;

@Getter
public class LeadCriteria {
    private String keyword;
    private long id;
    private long statusId;
    private long investmentGoalId;
    private long communicateWayId;
    private long cancelReasonsId;
    private long salesRepId;
    private long channelId;
    private long projectId;
    private boolean deleted;
    private long creatorId;
    private int page;
    private int size;
}
