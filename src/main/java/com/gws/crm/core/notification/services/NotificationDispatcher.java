package com.gws.crm.core.notification.services;

import com.gws.crm.core.notification.entities.CrmNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationDispatcher {

    private final List<NotificationChannel> channels;

    public void dispatch(CrmNotification crmNotification) {
        for (NotificationChannel channel : channels) {
            try {
                channel.send(crmNotification);
            } catch (Exception e) {
                log.error("Failed to send via {}: {}", channel.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
