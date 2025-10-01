package com.gws.crm.core.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class DashboardStatisticsDto {
    private int allLeads;
    private int duplicateLeads;
    private int freshLeads;
    private int coldCalls;
    private int pendingLeads;
    private int leadersPending;
    private int followUp;
    private int meeting;
    private int followAfterMeeting;
    private int rescheduleMeeting;
    private int cancellation;
    private int doneDeal;
    private int pipeline;
    private int resale;
    private int followUpNoAnswer;
    private int hold;
    private int coldCallNoAnswer;
    private int unreachable;
    private int reservation;
}
