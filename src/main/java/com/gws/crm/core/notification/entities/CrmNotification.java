package com.gws.crm.core.notification.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
@Getter
@Setter
public class CrmNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;

    private Long recipientId;

    private String recipientName;

    private String recipientEmail;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private NotificationType type;

    private long referenceId;

    private String referenceType;

    private String title;

    private String body;

    @Column(name = "is_read")
    private Boolean read;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    @Column(columnDefinition = "json")
    private String data;
}
