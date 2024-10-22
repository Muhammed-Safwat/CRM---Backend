package com.gws.crm.core.leads.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Setter
@Getter
@SuperBuilder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
public class Lead extends SalesLead {
    // Lead-specific fields or methods can go here (if needed)
}
