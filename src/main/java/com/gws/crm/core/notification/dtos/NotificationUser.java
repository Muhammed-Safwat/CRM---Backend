package com.gws.crm.core.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationUser {
    private long id;
    private String name;
    private String email;

    public NotificationUser(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static NotificationUser to(long id, String name) {
        return new NotificationUser(id, name);
    }

    public static NotificationUser to(long id, String name, String email) {
        return new NotificationUser(id, name, email);
    }
}
