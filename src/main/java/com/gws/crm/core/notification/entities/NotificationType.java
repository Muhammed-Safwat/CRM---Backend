package com.gws.crm.core.notification.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private boolean pushEnabled;

    private boolean emailEnabled;

    private boolean smsEnabled;

    private boolean systemNotificationEnabled;

    private String icon;

}
