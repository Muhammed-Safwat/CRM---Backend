package com.gws.crm.core.notification.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private long id;
    private String title;
    private String body;
    private long senderId;
    private String senderName;
    private long recipientId;
    private Boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private String type;
    private String icon;
    private String route;
    private long referenceId;
    private String referenceType;
    private String data;
}
