package com.gws.crm.core.notification.builder;

import com.google.gson.Gson;
import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.entities.NotificationTemplate;
import com.gws.crm.core.notification.entities.NotificationType;
import com.gws.crm.core.notification.enums.NotificationCode;
import com.gws.crm.core.notification.event.NotificationEvent;
import com.gws.crm.core.notification.repository.NotificationTemplateRepository;
import com.gws.crm.core.notification.repository.NotificationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationBuilder {

    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;


    public CrmNotification build(NotificationEvent event) {
        NotificationCode code = event.getCode();
        String codeStr = code.name();

        NotificationType type = notificationTypeRepository.findByCode(codeStr)
                .orElseThrow(() -> new RuntimeException("Notification type not found for code: " + codeStr));

        NotificationTemplate template = notificationTemplateRepository.findByCode(codeStr)
                .orElseThrow(() -> new RuntimeException("Notification template not found for code: " + codeStr));

        String title = replaceVariables(template.getTitle(), event);
        String body = replaceVariables(template.getBody(), event);

        return CrmNotification.builder()
                .senderId(event.getSenderId())
                .recipientId(event.getRecipientId())
                .recipientName(event.getRecipientName())
                .recipientEmail(event.getRecipientEmail())
                .title(title)
                .body(body)
                .read(false)
                .createdAt(LocalDateTime.now())
                .referenceId(event.getReferenceId())
                .referenceType(event.getReferenceType())
                .type(type)
                .data(new Gson().toJson(event.getData()))
                .build();
    }

    private String replaceVariables(String text, NotificationEvent event) {
        if (text == null) return "";

        // Replace fixed known values
        text = text
                .replace("{{senderName}}", safe(event.getSenderName()))
                .replace("{{recipientName}}", safe(event.getRecipientName()));

        // Replace values from data map using loop
        if (event.getData() != null) {
            for (Map.Entry<String, String> entry : event.getData().entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                text = text.replace(placeholder, safe(entry.getValue()));
            }
        }

        return text;
    }


    private String safe(String val) {
        return val != null ? val : "";
    }

}
