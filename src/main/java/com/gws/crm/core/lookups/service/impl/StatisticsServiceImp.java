package com.gws.crm.core.lookups.service.impl;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.StatisticsDTO;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.lookups.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImp implements StatisticsService {

    private final AreaRepository areaRepository;

    private final RegionRepository regionRepository;

    private final StageRepository stageRepository;

    private final CommunicateWayRepository communicateWayRepository;

    private final ChannelRepository channelRepository;

    private final ProjectRepository projectRepository;

    private final DevCompanyRepository devCompanyRepository;

    private final CancelReasonsRepository cancelReasonsRepository;

    public ResponseEntity<?> getStatistics(Transition transition) {
        StatisticsDTO statisticsDTO = StatisticsDTO.builder().totalChannels(channelRepository.countByAdminId(transition.getUserId()))
                .totalStage(stageRepository.countByAdminId(transition.getUserId()))
                .totalRegions(regionRepository.countByAdminId(transition.getUserId()))
                .totalCommunicateWays(communicateWayRepository.countByAdminId(transition.getUserId()))
                .totalAreas(areaRepository.countByAdminId(transition.getUserId()))
                .totalProjects(projectRepository.countByAdminId(transition.getUserId()))
                .totalDevCompanies(devCompanyRepository.countByAdminId(transition.getUserId()))
                .totalCancelReasons(cancelReasonsRepository.countByAdminId(transition.getUserId()))
                .build();
        return success(statisticsDTO);
    }


}
