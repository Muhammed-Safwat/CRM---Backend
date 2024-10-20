package com.gws.crm.core.employee.entity;

import lombok.Getter;

@Getter
public enum ActionType {
    LOGIN("Login"),
    LOGOUT("Logout"),
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
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
