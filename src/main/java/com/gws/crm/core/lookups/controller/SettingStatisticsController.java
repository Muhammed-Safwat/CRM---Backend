package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/statistics")
@RequiredArgsConstructor
public class SettingStatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<?> getStatistics(Transition transition) {
        return statisticsService.getStatistics(transition);
    }
}
