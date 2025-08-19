package com.gws.crm.common.properites;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class AppInfo {

    @Autowired
    private Environment env;
    private String name;
    private String email;
    private String webUrl;
    private String apiUrl;
    private String logo;
    private String defaultLanguage;
    private String termsAndCondLink;
    private String userResetPasswordUrl;

    @PostConstruct
    public void refresh() {
        this.name = env.getProperty("app.info.name");
        this.email = env.getProperty("app.info.email");
        this.webUrl = env.getProperty("app.info.web.url");
        this.apiUrl = env.getProperty("app.info.api.url");
        this.logo = env.getProperty("app.info.logo");
        this.defaultLanguage = env.getProperty("app.info.language");
        this.termsAndCondLink = env.getProperty("app.info.terms-and-conditions");
        this.userResetPasswordUrl = env.getProperty("app.info.user.rest-password.url");
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getTermsAndCondLink() {
        return termsAndCondLink;
    }

    public void setTermsAndCondLink(String termsAndCondLink) {
        this.termsAndCondLink = termsAndCondLink;
    }

    public String getUserResetPasswordUrl() {
        return userResetPasswordUrl;
    }

    public void setUserResetPasswordUrl(String userResetPasswordUrl) {
        this.userResetPasswordUrl = userResetPasswordUrl;
    }


}
