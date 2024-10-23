package com.gws.crm.core.notification.controller;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.gws.crm.core.notification.dtos.NotificationRequest;
import com.gws.crm.core.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerFcmToken(@RequestParam String token) {
        log.info("Received FCM token: " + token);
        log.info("Token received");
        return notificationService.registerToken(token);
    }

    @GetMapping
    public ResponseEntity<?> getAllNotification(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        return notificationService.getAllNotification(page, size);
    }

    @GetMapping("/count")
    public ResponseEntity<?> countNotification() {
        return notificationService.countClientNotification();
    }

    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) throws FirebaseMessagingException {
        return this.notificationService.sendNot(request);
    }

    @PutMapping
    public ResponseEntity<?> markAllAsRead() {
        return notificationService.markAllAsRead();
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> markAsRead(@PathVariable String notificationId) {
        return notificationService.markAsRead(notificationId);
    }

}
