package com.gws.crm.core.lookups.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Area extends BaseLookup {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "region_id")
    private Region region;

}