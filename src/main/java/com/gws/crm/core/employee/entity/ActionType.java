package com.gws.crm.core.employee.entity;

import lombok.Getter;

@Getter
public enum ActionType {
    LOGIN("Login"),
    LOGOUT("Logout"),

    CREATE("Create"),
    EDIT("Edit"),
    DELETE("Delete"),
    ASSIGN("Assign"),
    ANSWERED("Add Answer"),
    NO_ANSWER("No Answer"),

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
