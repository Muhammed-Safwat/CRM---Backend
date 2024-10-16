package com.gws.crm.core.lookups.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StatisticsDTO {
    private long totalChannels;
    private long totalStage;
    private long totalRegions;
    private long totalCommunicateWays;
    private long totalAreas;
    private long totalProjects;
    private long totalDevCompanies;
    private long totalCancelReasons;
    private long totalBrokers;
    private long totalCampaigns;
    private long totalCategories;
    private long totalInvestmentGoals;
    private long totalLeadStatuses;
    private long totalPropertyType;

}
