package com.gws.crm.core.notification.enums;

public enum ClientType {
    WEB,
    MOBILE,
    DESKTOP;

    public static ClientType fromString(String value) {
        if (value == null) return null;
        return ClientType.valueOf(value.trim().toUpperCase());
    }
}