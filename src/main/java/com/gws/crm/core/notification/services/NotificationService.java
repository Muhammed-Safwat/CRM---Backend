package com.gws.crm.core.notification.services;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.gws.crm.core.admin.entity.EventType;
import com.gws.crm.core.notification.dtos.NotificationRequest;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    ResponseEntity<?> registerToken(String token);

    ResponseEntity<?> getAllNotification(int page, int size);

    ResponseEntity<?> countClientNotification();

    ResponseEntity<String> sendNot(NotificationRequest request) throws FirebaseMessagingException;

    ResponseEntity<?> markAllAsRead();

    ResponseEntity<?> markAsRead(String notificationId);

    void notifyUser(EventType eventType, String message, String name, long id);
}
