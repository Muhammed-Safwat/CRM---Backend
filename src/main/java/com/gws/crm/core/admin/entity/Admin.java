package com.gws.crm.core.admin.entity;


import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.leads.entity.Lead;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@SuperBuilder
public class Admin extends User {

    // Maximum number of users (employees) the admin can create
    private int maxNumberOfUsers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "admin")
    private List<Employee> employees;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lead> leads;

}
