package com.gws.crm.core.notification.builder;

import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.notification.dtos.NotificationUser;
import com.gws.crm.core.notification.enums.NotificationCode;
import com.gws.crm.core.notification.enums.ReferenceType;
import com.gws.crm.core.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class LeadNotificationEventBuilder<T extends BaseLead> {

    public NotificationEvent build(T lead, NotificationUser sender,
                                   NotificationUser recipient,
                                   NotificationCode code,
                                   Map<String, String> data) {
        return NotificationEvent.builder()
                .code(code)
                .senderId(sender.getId())
                .senderName(sender.getName())
                .recipientId(recipient.getId())
                .recipientName(recipient.getName())
                .referenceId(lead.getId())
                .referenceType(ReferenceType.LEAD.name())
                .data(data)
                .recipientName(recipient.getName())
                .recipientEmail(recipient.getEmail())
                .build();
    }


}
