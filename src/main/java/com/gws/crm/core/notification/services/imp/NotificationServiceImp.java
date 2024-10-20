package com.gws.crm.core.notification.services.imp;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.gws.crm.core.admin.entity.EventType;
import com.gws.crm.core.notification.dtos.NotificationRequest;
import com.gws.crm.core.notification.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImp implements NotificationService {


    @Override
    public ResponseEntity<?> registerToken(String token) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAllNotification(int page, int size) {
        return null;
    }

    @Override
    public ResponseEntity<?> countClientNotification() {
        return null;
    }
    public void sendNotification() {
        Notification notification = Notification
                .builder()
                .setBody("Test Notification Body")
                .setTitle("Test Notification Title")
                .setImage("Test Notification Image")
                .build();

        // Set the message with the token and notification
        Message message = Message.builder()
                .setNotification(notification)
                .setToken("fVFfj3bSzn_KDX6aZZlY4s:APA91bEd65PJzKWrwGIbI3gZ6P6ub-ynjjv8p0B5hvYGPXVUsLGLBOAoVp3MFSll_QnoolEL1klpfOuKHWy-gPpxj2gqdNlC5qdKbzdOSO8_DY4-KtzrA3Y3TQs3ETDgCJVidI6zPUzC") // Example token
                .build();

        try {
            // Send the message
            String response = FirebaseMessaging.getInstance().send(message); // Sending the notification
            log.info("Successfully sent FCM message: {}", response);
        } catch (Exception e) {
            log.error("Error sending FCM message: {}", e.getMessage(), e);
        }
    }
    @Override
    public ResponseEntity<String> sendNot(NotificationRequest request) throws FirebaseMessagingException {
        sendNotification();
        if (true) {
            log.info("Sending notification to client with token: " + request.getToken());

            Notification notification = Notification
                    .builder()
                    .setBody("Test Notification Body")
                    .setTitle("Test Notification Title")
                    .setImage("Test Notification Image")
                    .build();

            Message message = Message.builder()
                    .setToken("fWcvSwCqS3WIrQZwv8RwHQ:APA91bG44kFw9DfE-gEEAQrc7YfdOmqujpC_XQAfh6UG58oDxLCVbxJQl6OBWv5EbhQE3LFB36dW2Zb80xxtlQAAS2HRMDsuMrXpv8QPZpIM5LEjrAaMb_pKfGfh0Xk3SDdc9bSGZniR")
                    .setNotification(notification)
                    .putData("type","Test Notification Type")
                    .putData("link","Test Notification link")
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("instance not null ",response);
                log.info("Notification sent successfully, response: " + response);
                return ResponseEntity.ok(response);
            } catch (FirebaseMessagingException e) {
                log.info("Failed to send notification" + e);
                throw e;
            }
        }
        log.info("Notification request was not processed");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Notification request was not processed");
    }

    @Override
    public ResponseEntity<?> markAllAsRead() {
        return null;
    }

    @Override
    public ResponseEntity<?> markAsRead(String notificationId) {
        return null;
    }

    @Override
    public void notifyUser(EventType eventType, String message, String name, long id) {

    }
}
