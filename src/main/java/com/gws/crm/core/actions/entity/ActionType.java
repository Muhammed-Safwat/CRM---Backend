package com.gws.crm.core.actions.entity;

import lombok.Getter;

@Getter
public enum ActionType {
    LOGIN("Login"),
    LOGOUT("Logout"),

    CREATE("Create"),
    EDIT("Edit"),
    DELETE("Delete"),
    RESTORE("Restore"),
    ASSIGN("Assign"),
    ANSWERED("Answered"),
    NO_ANSWER("No Answer"),
    DELAYED("Delayed"),
    VIEW("View"),
    ASSIGN_TASK("Assign Task"),
    EXPORT_DATA("Export Data"),
    IMPORT_DATA("Import Data"),
    GENERATE_REPORT("Generate Report");

    private final String displayValue;

    ActionType(String displayValue) {
        this.displayValue = displayValue;
    }

}
