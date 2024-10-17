package com.gws.crm.core.lookups.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
public class Broker extends BaseLookup {

}
