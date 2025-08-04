package com.gws.crm.core.leads.service.imp;

import com.gws.crm.core.leads.repository.BaseLeadRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LeadMonitoringService {

    private final BaseLeadRepository leadRepository;

    public LeadMonitoringService(BaseLeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public void checkAndUpdateDelayedLeads() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdTime = now.minusHours(3);

         leadRepository.markDelayedLeads(thresholdTime, now);
    }
}
