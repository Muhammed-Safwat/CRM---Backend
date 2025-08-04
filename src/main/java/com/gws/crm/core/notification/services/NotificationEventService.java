package com.gws.crm.core.notification.services;

import com.gws.crm.core.notification.event.NotificationEvent;

public interface NotificationEventService {
    void handel(NotificationEvent notificationEvent);
}
