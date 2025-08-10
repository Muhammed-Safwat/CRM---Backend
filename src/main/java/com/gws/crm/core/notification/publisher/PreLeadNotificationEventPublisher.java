package com.gws.crm.core.notification.publisher;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.notification.builder.LeadNotificationEventBuilder;
import com.gws.crm.core.notification.enums.NotificationCode;
import com.gws.crm.core.notification.event.NotificationEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.gws.crm.core.notification.dtos.NotificationUser.to;

@Component
@AllArgsConstructor
public class PreLeadNotificationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final LeadNotificationEventBuilder<PreLead> leadNotificationEventBuilder;

    public void publishCreateLeadEvent(PreLead lead, Transition transition) {
        // create lead but not admin
        Map<String, String> data = Map.of(
                "leadName", lead.getName(),
                "projectName", lead.getProject().getName()
        );
        if (transition.getUserId() == lead.getAdmin().getId()) {
            return;
        }
        NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(
                lead,
                to(transition.getUserId(), transition.getUserName()),
                to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                NotificationCode.LEAD_CREATED,
                data
        );
        eventPublisher.publishEvent(eventToAdmin);
    }


    public void publishDeleteLeadEvent(PreLead lead, Transition transition) {
        Map<String, String> data = Map.of(
                "leadName", lead.getName()
        );
        if (transition.getUserId() == lead.getAdmin().getId()) return;
        // admin delete lead
        NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(
                lead,
                to(transition.getUserId(), transition.getUserName()),
                to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                NotificationCode.LEAD_UPDATED,
                data
        );
        eventPublisher.publishEvent(eventToAdmin);
    }

    public void publishEditLeadEvent(PreLead lead, Transition transition) {
        // admin update lead
        Map<String, String> data = Map.of(
                "leadName", lead.getName()
        );
        if (transition.getUserId() == lead.getAdmin().getId()) return;

        NotificationEvent eventToSales = leadNotificationEventBuilder
                .build(lead,
                        to(transition.getUserId(), transition.getUserName()),
                        to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                        NotificationCode.LEAD_UPDATED, data);
        eventPublisher.publishEvent(eventToSales);
    }


    public void publishRestoreLeadEvent(PreLead lead, Transition transition) {
        // admin restore lead
        Map<String, String> data = Map.of(
                "leadName", lead.getName()
        );

        if (transition.getUserId() == lead.getAdmin().getId()) return;

        NotificationEvent event = leadNotificationEventBuilder.build(lead,
                to(transition.getUserId(), transition.getUserName()),
                to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                NotificationCode.LEAD_RESTORED,
                data
        );
        eventPublisher.publishEvent(event);
    }
}
