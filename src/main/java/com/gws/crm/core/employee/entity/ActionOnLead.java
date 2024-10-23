package com.gws.crm.core.employee.entity;


import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.lookups.entity.CallOutcome;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@SuperBuilder
public class ActionOnLead extends UserAction {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = false)
    private SalesLead lead;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private CallOutcome callOutcome;

    private LocalDateTime nextActionDate;

    private String cancellationReason;

    private String stage;

    private LocalDateTime callBackTime;

}
