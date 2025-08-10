package com.gws.crm.core.notification.services.imp;

import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.services.EmailTemplateService;
import com.gws.crm.core.notification.services.NotificationChannel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationChannel implements NotificationChannel {

    private final JavaMailSender mailSender;

    private final EmailTemplateService emailTemplateService;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void send(CrmNotification crmNotification) throws IOException {
        if (!crmNotification.getType().isEmailEnabled()) return;

        String htmlContent = emailTemplateService.generateHtmlTemplate(crmNotification);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(crmNotification.getRecipientEmail());
            helper.setSubject(crmNotification.getTitle());
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
        }
    }

}