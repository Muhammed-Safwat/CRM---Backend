package com.gws.crm.common.entities;


import com.gws.crm.common.constant.Language;
import lombok.Getter;

@Getter
public class Transition {

    private final long id;
    private final Long userId;
    private final String role;
    private final Language language;
    private final int os;
    private final String version;

    public Transition() {
        this.id = System.currentTimeMillis();
        this.language = Language.EN; // Default language or fetch from context
        this.userId = null;
        this.os = 0; // OS identifier
        this.version = ""; // Application version or context-specific
        this.role = "";
    }

    public Transition(Language language, int os, String version) {
        this.role = "";
        this.id = System.currentTimeMillis();
        this.language = language;
        this.userId = null;
        this.os = os;
        this.version = version;
    }

    public Transition(Language language, Long userId, String role, int os, String version) {
        this.role = role;
        this.id = System.currentTimeMillis();
        this.language = language;
        this.userId = userId;
        this.os = os;
        this.version = version;
    }


}
