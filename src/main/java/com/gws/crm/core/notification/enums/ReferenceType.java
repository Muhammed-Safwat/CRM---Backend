package com.gws.crm.core.notification.enums;

public enum ReferenceType {

    LEAD,
    ADMIN,
    COMMENT ,
    TASK ;

    public String getCode() {
        return this.name();
    }
}
