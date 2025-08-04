package com.gws.crm.core.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class NotificationUser {
    private long id ;
    private String name ;

    public static NotificationUser to(long id ,String name){
         return new NotificationUser(id,name);
    }
}
