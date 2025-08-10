package com.gws.crm.core.notification.publisher;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.notification.builder.LeadNotificationEventBuilder;
import com.gws.crm.core.notification.enums.NotificationCode;
import com.gws.crm.core.notification.event.NotificationEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.gws.crm.core.notification.dtos.NotificationUser.to;


@Component
@AllArgsConstructor
public class TeleSalesLeadNotificationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final LeadNotificationEventBuilder<TeleSalesLead> leadNotificationEventBuilder;

    public void publishCreateLeadEvent(TeleSalesLead lead, Transition transition) {

        // create lead but not admin
        Map<String, String> data = new HashMap<>();
        data.put("leadName", lead.getName());
        data.put("projectName", lead.getProject().getName());
        if (transition.getUserId() != lead.getAdmin().getId()) {

            NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(
                    lead,
                    to(transition.getUserId(), transition.getUserName()),
                    to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                    NotificationCode.LEAD_CREATED,
                    data
            );
            eventPublisher.publishEvent(eventToAdmin);
        }
        if (lead.getSalesRep() != null) {
            publishAssignLeadEvent(lead, null, transition);
        }
    }


    public void publishDeleteLeadEvent(TeleSalesLead lead, Transition transition) {
        // admin delete lead
        Map<String, String> data = new HashMap<>();
        data.put(
                "leadName", lead.getName()
        );
        if (transition.getUserId() == lead.getAdmin().getId()) {
            NotificationEvent event = leadNotificationEventBuilder.build(
                    lead,
                    to(transition.getUserId(), transition.getUserName()),
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName(), lead.getSalesRep().getUsername()),
                    NotificationCode.LEAD_UPDATED,
                    data
            );
            eventPublisher.publishEvent(event);
        } else if (transition.getUserId() == lead.getSalesRep().getId()) {

            NotificationEvent event = leadNotificationEventBuilder.build(
                    lead,
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName()),
                    to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                    NotificationCode.LEAD_UPDATED,
                    data
            );
            eventPublisher.publishEvent(event);
        } else {
            NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(
                    lead,
                    to(transition.getUserId(), transition.getUserName()),
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName(), lead.getSalesRep().getUsername()),
                    NotificationCode.LEAD_UPDATED,
                    data
            );
            NotificationEvent eventToSales = leadNotificationEventBuilder.build(
                    lead,
                    to(transition.getUserId(), transition.getUserName()),
                    to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                    NotificationCode.LEAD_UPDATED,
                    data
            );
            eventPublisher.publishEvent(eventToAdmin);
            eventPublisher.publishEvent(eventToSales);
        }

    }


    public void publishEditLeadEvent(TeleSalesLead lead, Transition transition) {
        Map<String, String> data = new HashMap<>();
        data.put(
                "leadName", lead.getName()
        );
        // admin update lead
        if (transition.getUserId() == lead.getAdmin().getId()) {
            NotificationEvent event = leadNotificationEventBuilder.build(lead,
                    to(transition.getUserId(), transition.getUserName()),
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName(), lead.getSalesRep().getUsername()),
                    NotificationCode.LEAD_UPDATED, data);
            eventPublisher.publishEvent(event);
        } else if (transition.getUserId() == lead.getSalesRep().getId()) {
            // sales update lead

            NotificationEvent event = leadNotificationEventBuilder.build(lead,
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName()),
                    to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                    NotificationCode.LEAD_UPDATED, null);
            eventPublisher.publishEvent(event);
        } else {
            // manger update lead
            NotificationEvent eventToAdmin = leadNotificationEventBuilder
                    .build(lead,
                            to(transition.getUserId(), transition.getUserName()),
                            to(lead.getSalesRep().getId(), lead.getSalesRep().getName(), lead.getSalesRep().getUsername()),
                            NotificationCode.LEAD_UPDATED, data);
            eventPublisher.publishEvent(eventToAdmin);
            NotificationEvent eventToSales = leadNotificationEventBuilder
                    .build(lead,
                            to(transition.getUserId(), transition.getUserName()),
                            to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                            NotificationCode.LEAD_UPDATED, data);
            eventPublisher.publishEvent(eventToSales);
        }

    }


    public void publishRestoreLeadEvent(TeleSalesLead lead, Transition transition) {

        // admin restore lead
        Map<String, String> data = new HashMap<>();
        data.put(
                "leadName", lead.getName()
        );
        if (transition.getUserId() == lead.getAdmin().getId()) {
            NotificationEvent event = leadNotificationEventBuilder.build(lead,
                    to(transition.getUserId(), transition.getUserName()),
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName(), lead.getSalesRep().getUsername()),
                    NotificationCode.LEAD_RESTORED,
                    data
            );
            eventPublisher.publishEvent(event);
        } else if (transition.getUserId() == lead.getSalesRep().getId()) {
            // sales restore lead
            NotificationEvent event = leadNotificationEventBuilder.build(lead,
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName()),
                    to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                    NotificationCode.LEAD_RESTORED,
                    data
            );
            eventPublisher.publishEvent(event);
        } else {
            // manger restore lead
            NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(lead, to(transition.getUserId(),
                            transition.getUserName()),
                    to(lead.getSalesRep().getId(),
                            lead.getSalesRep().getName()), NotificationCode.LEAD_RESTORED, data);
            NotificationEvent eventToSales = leadNotificationEventBuilder.build(lead,
                    to(transition.getUserId(),
                            transition.getUserName()),
                    to(lead.getSalesRep().getId(), lead.getSalesRep().getName(), lead.getSalesRep().getUsername()),
                    NotificationCode.LEAD_RESTORED, data);
            eventPublisher.publishEvent(eventToSales);
            eventPublisher.publishEvent(eventToAdmin);
        }
    }


    public void publishAssignLeadEvent(TeleSalesLead lead, Employee lastSales, Transition transition) {

        // for new sales
        Map<String, String> data = new HashMap<>();
        data.put("leadName", lead.getName());
        data.put("projectName", lead.getProject().getName());
        NotificationEvent event = leadNotificationEventBuilder.build(lead, to(transition.getUserId(),
                        transition.getUserName()), to(lead.getSalesRep().getId(), lead.getSalesRep().getName()),
                NotificationCode.ASSIGN_SALES_TO_LEAD, data);
        eventPublisher.publishEvent(event);
        // not admin one of mangers
        if (lead.getAdmin().getId() != transition.getUserId()) {

            NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(lead,
                    to(transition.getUserId(),
                            transition.getUserName()),
                    to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                    NotificationCode.ASSIGN_SALES_TO_LEAD, data);
            eventPublisher.publishEvent(eventToAdmin);
        }
        // notification for last sales
        if (lastSales != null) {
            data.put("email", lastSales.getUsername());
            NotificationEvent leadTransferEvent = leadNotificationEventBuilder.build(lead,
                    to(transition.getUserId(), transition.getUserName()),
                    to(lastSales.getId(), lastSales.getName(), lastSales.getUsername()),
                    NotificationCode.LEAD_TRANSFER,
                    data);
            eventPublisher.publishEvent(leadTransferEvent);
        }
    }


    public void publishViewLeadEvent(TeleSalesLead lead, Transition transition) {
        // send this event once (first time reviewed by sales)
        Map<String, String> data = new HashMap<>();
        data.put(
                "leadName", lead.getName());
        NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(lead,
                to(transition.getUserId(),
                        transition.getUserName()),
                to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                NotificationCode.SALES_REVIWE_LEAD, data);
        eventPublisher.publishEvent(eventToAdmin);
    }


    public void publishDelayLeadEvent(TeleSalesLead lead, Transition transition) {
        // when delayed event occur send notification to sales admin
        Map<String, String> data = Map.of(
                "leadName", lead.getName()
        );

        NotificationEvent eventToAdmin = leadNotificationEventBuilder.build(lead,
                to(lead.getSalesRep().getId(),
                        lead.getSalesRep().getName()),
                to(lead.getAdmin().getId(), lead.getAdmin().getName(), lead.getAdmin().getUsername()),
                NotificationCode.LEAD_DELAYED, data);
        eventPublisher.publishEvent(eventToAdmin);
    }
}
