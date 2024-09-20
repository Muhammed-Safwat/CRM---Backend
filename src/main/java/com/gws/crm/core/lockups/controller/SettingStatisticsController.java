package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class SettingStatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("statistics")
    public ResponseEntity<?> getStatistics() {
        return statisticsService.getStatistics();
    }
}
