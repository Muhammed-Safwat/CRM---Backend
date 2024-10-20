package com.gws.crm.core.employee.entity;

import com.gws.crm.authentication.entity.PrivilegeGroup;
import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.entity.EventType;
import com.gws.crm.core.notification.services.NotificationService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@SuperBuilder
public class Employee extends User {

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "job_name_id")
    private PrivilegeGroup jobName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Override
    public void notify(EventType eventType, String message, NotificationService notificationService) {
        notificationService.notifyUser(eventType,message,getName(),getId());
    }
}
