package com.gws.crm.core.lockups.service.impl;


import com.gws.crm.core.lockups.dto.StatisticsDTO;
import com.gws.crm.core.lockups.repository.*;
import com.gws.crm.core.lockups.service.StatisticsService;
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

    public ResponseEntity<?> getStatistics() {
        StatisticsDTO statisticsDTO = StatisticsDTO.builder().totalChannels(channelRepository.count())
                .totalStage(stageRepository.count())
                .totalRegions(regionRepository.count())
                .totalCommunicateWays(communicateWayRepository.count())
                .totalAreas(areaRepository.count())
                .totalProjects(projectRepository.count())
                .totalDevCompanies(devCompanyRepository.count())
                .build();
        return success(statisticsDTO);
    }
}
