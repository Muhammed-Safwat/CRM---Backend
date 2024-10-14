package com.gws.crm.core.lookups.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@SuperBuilder
@NoArgsConstructor
public class Area extends BaseLookup {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "region_id")
    private Region region;

}
