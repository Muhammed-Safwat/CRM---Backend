package com.gws.crm.core.notification.services.imp;

import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.services.NotificationChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationChannel implements NotificationChannel {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void send(CrmNotification crmNotification) {
        if (!crmNotification.getType().isEmailEnabled()) return;

        // Your actual email logic here
        System.out.println("📧 Sending email to " + crmNotification.getRecipientId());
    }
}