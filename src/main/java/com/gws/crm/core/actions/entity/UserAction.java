package com.gws.crm.core.actions.entity;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.employee.entity.Admin;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    private String creatorName ;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ActionType type;

    @Column(length = 1000)
    private String description;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userAction", cascade = CascadeType.ALL, orphanRemoval = true)
    private LeadActionDetails leadDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;
}
