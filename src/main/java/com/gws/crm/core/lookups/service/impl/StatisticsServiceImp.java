package com.gws.crm.core.lookups.service.impl;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.StatisticsDTO;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.lookups.service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@AllArgsConstructor
@Slf4j
public class StatisticsServiceImp implements StatisticsService {

    private final AreaRepository areaRepository;
    private final RegionRepository regionRepository;
    private final StageRepository stageRepository;
    private final CommunicateWayRepository communicateWayRepository;
    private final ChannelRepository channelRepository;
    private final ProjectRepository projectRepository;
    private final DevCompanyRepository devCompanyRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final BrokerRepository brokerRepository;
    private final CampaignRepository campaignRepository;
    private final CategoryRepository categoryRepository;
    private final InvestmentGoalRepository investmentGoalRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final PropertyTypeRepository propertyTypeRepository;
    private final CallOutcomeRepository callOutcomeRepository;

    public ResponseEntity<?> getStatistics(Transition transition) {
        StatisticsDTO statisticsDTO = StatisticsDTO.builder()
                .totalNextActions(callOutcomeRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalChannels(channelRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalStage(stageRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalRegions(regionRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalCommunicateWays(communicateWayRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalAreas(areaRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalProjects(projectRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalDevCompanies(devCompanyRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalCancelReasons(cancelReasonsRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalBrokers(brokerRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalCampaigns(campaignRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalCategories(categoryRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalInvestmentGoals(investmentGoalRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalLeadStatuses(leadStatusRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .totalPropertyType(propertyTypeRepository.countByAdminIdAndDeletedFalse(transition.getUserId()))
                .build();

        return success(statisticsDTO);
    }


}
