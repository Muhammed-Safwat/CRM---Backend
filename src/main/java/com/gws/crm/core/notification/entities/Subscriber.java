package com.gws.crm.core.notification.entities;

import com.gws.crm.core.admin.entity.EventType;
import com.gws.crm.core.notification.services.NotificationService;

public interface Subscriber {

    void notify(EventType eventType , String message, NotificationService notificationService);
}
