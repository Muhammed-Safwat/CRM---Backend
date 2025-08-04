package com.gws.crm.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@EnableAsync
public class appConfig extends AcceptHeaderLocaleResolver {

}
