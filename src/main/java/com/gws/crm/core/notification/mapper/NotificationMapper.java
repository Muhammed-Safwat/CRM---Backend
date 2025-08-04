package com.gws.crm.core.notification.mapper;

import com.gws.crm.core.notification.dtos.NotificationDTO;
import com.gws.crm.core.notification.entities.CrmNotification;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationMapper {

    public static NotificationDTO toDto(CrmNotification crmNotification) {
        return NotificationDTO.builder()
                .id(crmNotification.getId())
                .title(crmNotification.getTitle())
                .body(crmNotification.getBody())
                .senderId(crmNotification.getSenderId())
                .recipientId(crmNotification.getRecipientId())
                .read(crmNotification.getRead())
                .createdAt(crmNotification.getCreatedAt())
                .readAt(crmNotification.getReadAt())
                .referenceId(crmNotification.getReferenceId())
                .referenceType(crmNotification.getReferenceType())
                .type(crmNotification.getType().getCode())
                .icon(crmNotification.getType().getIcon())
                .data(crmNotification.getData())
                .build();
    }

    public static List<NotificationDTO> toDto(List<CrmNotification> crmNotifications) {
        return crmNotifications.stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<NotificationDTO> toDto(Page<CrmNotification> page) {
        return page.map(NotificationMapper::toDto);
    }
}
