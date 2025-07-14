package com.gws.crm.core.employee.entity;


import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.lookups.entity.CallOutcome;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class LeadActionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_action_id", nullable = false)
    private UserAction userAction;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = false)
    private BaseLead lead;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private CallOutcome callOutcome;

    private LocalDateTime nextActionDate;

    private String cancellationReason;

    private String stage;

    private LocalDateTime callBackTime;

    @Column(columnDefinition = "TEXT")
    private String comment;
}
