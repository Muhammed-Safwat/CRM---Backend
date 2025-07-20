package com.gws.crm.core.employee.entity;

import com.gws.crm.authentication.entity.User;
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ActionType type;

    private String description;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userAction", cascade = CascadeType.ALL, orphanRemoval = true)
    private LeadActionDetails leadDetails;

}
