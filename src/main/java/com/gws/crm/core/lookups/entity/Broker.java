package com.gws.crm.core.lookups.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gws.crm.core.admin.entity.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
public class Broker  extends BaseLookup{

}
