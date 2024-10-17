package com.gws.crm.core.leads.entity;


import jakarta.persistence.Entity;
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

    private String link;


}
