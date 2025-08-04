package com.gws.crm.core.notification.services;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.notification.dtos.RegistrationTokenReq;
import org.springframework.http.ResponseEntity;



public interface NotificationService {
    ResponseEntity<?> registerToken(RegistrationTokenReq registrationTokenReq, Transition transition);

    ResponseEntity<?> getAllNotification(int page, int size, Transition transition);

    ResponseEntity<?> countClientNotification(Transition transition);

    ResponseEntity<?> sendNot();

    ResponseEntity<?> markAllAsRead(Transition transition);

    ResponseEntity<?> markAsRead(long notificationId, Transition transition);

    ResponseEntity<?> deleteNotification(long notificationId, Transition transition);
}
