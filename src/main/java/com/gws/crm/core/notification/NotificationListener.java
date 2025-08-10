package com.gws.crm.core.notification;

import com.gws.crm.core.notification.event.NotificationEvent;
import com.gws.crm.core.notification.services.NotificationEventService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class NotificationListener {

    private final NotificationEventService notificationService;

    @Async
    @EventListener
    public void onNotificationEvent(NotificationEvent event) {
        notificationService.handel(event);
    }


}
