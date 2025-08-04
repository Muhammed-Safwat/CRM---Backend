package com.gws.crm.core.notification.event;

 import com.gws.crm.core.notification.enums.NotificationCode;
import lombok.AllArgsConstructor;
 import lombok.Builder;
 import lombok.Getter;
 import lombok.ToString;

 import java.util.Map;



@Getter
@AllArgsConstructor
@ToString
@Builder
public class NotificationEvent {
    private final Long senderId;
    private long referenceId;
    private Long recipientId;
    public final NotificationCode code;
    public final Map<String, String> data;
    private String referenceType;
    private String senderName ;
    private String recipientName;

}