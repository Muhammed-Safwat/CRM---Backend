package com.gws.crm.core.notification.enums;

public enum NotificationCode {

    ASSIGN_SALES_TO_LEAD,
    LEAD_RETURNED_TO_ADMIN,
    LEAD_COMMENT_ADDED,
    LEAD_DELAYED,
    LEAD_CLOSED,
    LEAD_CREATED,
    TASK_ASSIGNED,
    TASK_COMPLETED,
    LEAD_TRANSFER,
    LEAD_DELETED,
    LEAD_UPDATED,
    LEAD_RESTORED, SALES_REVIWE_LEAD;

    public String getCode() {
        return this.name();
    }
}
