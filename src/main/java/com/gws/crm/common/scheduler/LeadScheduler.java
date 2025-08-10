package com.gws.crm.common.scheduler;

import com.gws.crm.core.leads.service.imp.LeadMonitoringService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LeadScheduler {

    private final LeadMonitoringService leadMonitoringService;

   @Scheduled(fixedRate = 60000) // 1min
    public void scheduleCheckDelayedLeads() {
        leadMonitoringService.checkAndUpdateDelayedLeads();
    }


    @Scheduled(fixedRate = 300000) // 5 minutes - تردد أقل لأنه تحذير مسبق
    public void scheduleCheckLeadsNearingDelay() {
        leadMonitoringService.checkAndNotifyLeadsNearingDelay();
    }



}
