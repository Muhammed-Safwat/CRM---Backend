package com.gws.crm.core.notification.services.imp;

import com.google.firebase.messaging.*;
import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.entities.NotificationToken;
import com.gws.crm.core.notification.repository.NotificationRepository;
import com.gws.crm.core.notification.repository.NotificationTokenRepository;
import com.gws.crm.core.notification.services.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class PushNotificationChannel implements NotificationChannel {

    private final NotificationRepository notificationRepository;
    private final NotificationTokenRepository notificationTokenRepository;


    @Override
    public void send(CrmNotification crmNotification) throws FirebaseMessagingException {
        if (!crmNotification.getType().isPushEnabled()) return;

        // Save to database
        notificationRepository.save(crmNotification);

        List<NotificationToken> deviceToken = getDeviceTokenForUser(crmNotification.getRecipientId());
        if (deviceToken.isEmpty()) {
            log.warn("No device token found for recipientId {}", crmNotification.getRecipientId());
            return;
        }

        // Build and send push notification
        List<Message> message = buildFcmMessage(crmNotification, deviceToken);
        BatchResponse response = FirebaseMessaging.getInstance().sendEach(message);
        response.getResponses().forEach(res->{
            log.info("✅ Push notification sent. FCM response: {}", res );
        });


    }


    private List<Message> buildFcmMessage(CrmNotification crmNotification, List<NotificationToken> userTokens) {
        List<Message> messages = new ArrayList<>();
        for(NotificationToken notificationToken: userTokens){
            messages.add(Message.builder()
                    .setToken(notificationToken.getToken())
                    .setNotification(Notification.builder()
                            .setTitle(crmNotification.getTitle())
                            .setBody(crmNotification.getBody())
                            .build())
                    .putData("referenceId", String.valueOf(crmNotification.getReferenceId()))
                    .putData("referenceType", crmNotification.getReferenceType())
                    .build());
        }
        return messages;
    }


    private List<NotificationToken> getDeviceTokenForUser(Long userId) {
        return notificationTokenRepository.findByUserId(userId);
    }
}
