package com.gws.crm.core.notification.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.notification.dtos.RegistrationTokenReq;
import com.gws.crm.core.notification.services.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@Log
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerFcmToken(@RequestBody RegistrationTokenReq registrationTokenReq,
                                              Transition transition) {
        log.info("Received FCM token: " + registrationTokenReq.getToken());
        log.info("Token received");
        return notificationService.registerToken(registrationTokenReq, transition);
    }

    @GetMapping
    public ResponseEntity<?> getAllNotification(@RequestParam("page") int page,
                                                @RequestParam("size") int size,
                                                Transition transition) {
        log.info(transition.getUserName() + " ==> ****************************88");
        return notificationService.getAllNotification(page, size, transition);
    }

    @GetMapping("/count")
    public ResponseEntity<?> countNotification(Transition transition) {
        return notificationService.countClientNotification(transition);
    }

    @PutMapping
    public ResponseEntity<?> markAllAsRead(Transition transition) {
        return notificationService.markAllAsRead(transition);
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> markAsRead(@PathVariable long notificationId, Transition transition) {
        return notificationService.markAsRead(notificationId, transition);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable long notificationId, Transition transition) {
        return notificationService.deleteNotification(notificationId, transition);
    }
}