package com.gws.crm.core.dashboard.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.dashboard.service.DashboardService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("count-by-status")
    public ResponseEntity<?> countByStage(@Param("userId") Long userId , Transition transition){
        return  null ;
    }

}
