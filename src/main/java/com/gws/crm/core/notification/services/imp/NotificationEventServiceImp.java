package com.gws.crm.core.notification.services.imp;

import com.gws.crm.core.notification.builder.NotificationBuilder;
import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.event.NotificationEvent;
import com.gws.crm.core.notification.services.NotificationDispatcher;
import com.gws.crm.core.notification.services.NotificationEventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationEventServiceImp implements NotificationEventService {

    private final NotificationBuilder notificationBuilder;
    // private final NotificationRepository notificationRepository;
    private final NotificationDispatcher notificationDispatcher;

    @Override
    public void handel(NotificationEvent notificationEvent) {
        CrmNotification crmNotification =  notificationBuilder.build(notificationEvent);
        // notificationRepository.save(notification);
        notificationDispatcher.dispatch(crmNotification);
    }

}
