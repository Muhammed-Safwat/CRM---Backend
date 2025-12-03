package com.gws.crm.core.leads.service.imp;

import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.enums.NotificationCode;
import com.gws.crm.core.notification.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LeadMonitoringService {
/*
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


            List<BaseLead> delayedLeads = getDelayedLeads();


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

    private void sendDelayedNotification(BaseLead lead) {
        try {
            NotificationEvent event = NotificationEvent.builder()
                    .code(NotificationCode.LEAD_DELAYED)
                    .senderId(0L) // system BOT
                    .senderName("System")
                    .recipientId(lead.get().getId())
                    .recipientName(lead.getAssignedTo().getFullName())
                    .recipientEmail(lead.getAssignedTo().getEmail())
                    .referenceId(lead.getId())
                    .referenceType("LEAD")
                    .data(Map.of(
                            "leadName", lead.getName(),
                            "leadId", String.valueOf(lead.getId())
                    ))
                    .build();

            CrmNotification notification = notificationBuilder.build(event);
            notificationRepository.save(notification);

            log.info("Sent delayed notification for lead {}", lead.getId());

        } catch (Exception e) {
            log.error("Error sending delayed notification for lead {}", lead.getId(), e);
        }
    }*/

}
