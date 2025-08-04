package com.gws.crm.core.admin.entity;

import com.gws.crm.authentication.entity.User;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SuperAdmin extends User {


}
