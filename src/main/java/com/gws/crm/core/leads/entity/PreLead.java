package com.gws.crm.core.leads.entity;


import com.gws.crm.core.lookups.entity.Campaign;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@SuperBuilder
public class PreLead extends BaseLead {

    private boolean imported;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;


}
