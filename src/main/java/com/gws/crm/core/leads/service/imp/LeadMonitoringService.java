package com.gws.crm.core.leads.service.imp;

import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LeadMonitoringService {

    private final BaseLeadRepository leadRepository;

    public LeadMonitoringService(BaseLeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Transactional
    public void checkAndUpdateDelayedLeads() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdTime = now.minusHours(3); // 3 hours threshold

        int updatedLeads = leadRepository.markDelayedLeads(thresholdTime, now);

        if (updatedLeads > 0) {
            log.info("Marked {} leads as delayed", updatedLeads);

            // الحصول على قائمة الـ delayed leads لإرسال الإشعارات
            List<BaseLead> delayedLeads = getDelayedLeads();

            // i will send notification here
            // delayedLeads.forEach(this::sendDelayedNotification);
        }
    }

    @Transactional(readOnly = true)
    public void checkAndNotifyLeadsNearingDelay() {
        LocalDateTime now = LocalDateTime.now();
        List<BaseLead> leadsNearingDelay = getLeadsNearingDelay();

        if (!leadsNearingDelay.isEmpty()) {
            log.info("Found {} leads nearing delay, sending warning notifications",
                    leadsNearingDelay.size());

            // i will send notification here
            // leadsNearingDelay.forEach(this::sendWarningNotification);
        }
    }

    public List<BaseLead> getDelayedLeads() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdTime = now.minusHours(3);

        return leadRepository.findDelayedLeads(thresholdTime, now);
    }

    public List<BaseLead> getLeadsNearingDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime warningThreshold = now.minusHours(2);
        LocalDateTime createdAfter = now.minusHours(2).minusMinutes(5);

        return leadRepository.findLeadsNearingDelay(createdAfter, warningThreshold, now);
    }
}
