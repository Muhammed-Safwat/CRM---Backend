package com.gws.crm.core.notification.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private NotificationType type;

    @Lob
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;
}
