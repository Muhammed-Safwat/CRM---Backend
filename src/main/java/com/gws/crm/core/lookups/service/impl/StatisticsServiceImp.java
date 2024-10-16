package com.gws.crm.core.lookups.service.impl;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.StatisticsDTO;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.lookups.service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> getStatistics(Transition transition) {
        StatisticsDTO statisticsDTO = StatisticsDTO.builder()
                .totalChannels(channelRepository.countByAdminId(transition.getUserId()))
                .totalStage(stageRepository.countByAdminId(transition.getUserId()))
                .totalRegions(regionRepository.countByAdminId(transition.getUserId()))
                .totalCommunicateWays(communicateWayRepository.countByAdminId(transition.getUserId()))
                .totalAreas(areaRepository.countByAdminId(transition.getUserId()))
                .totalProjects(projectRepository.countByAdminId(transition.getUserId()))
                .totalDevCompanies(devCompanyRepository.countByAdminId(transition.getUserId()))
                .totalCancelReasons(cancelReasonsRepository.countByAdminId(transition.getUserId()))
                .totalBrokers(brokerRepository.countByAdminId(transition.getUserId()))
                .totalCampaigns(campaignRepository.countByAdminId(transition.getUserId()))
                .totalCategories(categoryRepository.countByAdminId(transition.getUserId()))
                .totalInvestmentGoals(investmentGoalRepository.countByAdminId(transition.getUserId()))
                .totalLeadStatuses(leadStatusRepository.countByAdminId(transition.getUserId()))
                .totalPropertyType(propertyTypeRepository.countByAdminId(transition.getUserId()))
                .build();

        return success(statisticsDTO);
    }


}
