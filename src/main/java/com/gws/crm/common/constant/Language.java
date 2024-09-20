package com.gws.crm.common.constant;

public enum Language {
    EN("en"), AR("ar");

    private String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
