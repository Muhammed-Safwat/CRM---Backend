package com.gws.crm.core.notification.services;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.gws.crm.core.notification.entities.CrmNotification;

public interface NotificationChannel {
    void send(CrmNotification crmNotification) throws FirebaseMessagingException;
}
