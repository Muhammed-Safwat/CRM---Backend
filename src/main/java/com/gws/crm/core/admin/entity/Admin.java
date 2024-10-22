package com.gws.crm.core.admin.entity;


import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.notification.entities.Subscriber;
import com.gws.crm.core.notification.services.NotificationService;
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
import java.util.Map;

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
    private List<BaseLead> leads;


    @Override
    public void notify(EventType eventType, String message, NotificationService notificationService) {

    }
}
