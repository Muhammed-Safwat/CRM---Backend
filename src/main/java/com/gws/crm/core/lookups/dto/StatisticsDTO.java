package com.gws.crm.core.lookups.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsDTO {
    private long totalRegions;
    private long totalAreas;
    private long totalChannels;
    private long totalCommunicateWays;
    private long totalStage;
    private long totalDevCompanies;
    private long totalProjects;
    private long totalCancelReasons;
    private long totalBrokers;
    private long totalCampaigns;
    private long totalCategories;
    private long totalInvestmentGoals;
    private long totalLeadStatuses;

}
