package com.gws.crm.core.employee.entity;

import lombok.Getter;

@Getter
public enum ActionStatus {
    SUCCESS("Success"),
    FAILURE("Failure"),
    PENDING("Pending");

    private final String displayValue;

    ActionStatus(String displayValue) {
        this.displayValue = displayValue;
    }

}
