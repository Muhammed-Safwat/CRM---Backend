package com.gws.crm.core.notification.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "notification_templates")
@Getter
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String language;

    private String title;

    private String body;
}
