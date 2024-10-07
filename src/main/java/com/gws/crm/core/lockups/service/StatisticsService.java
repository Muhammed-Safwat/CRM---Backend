package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

public interface StatisticsService {
    ResponseEntity<?> getStatistics(Transition transition);
}
