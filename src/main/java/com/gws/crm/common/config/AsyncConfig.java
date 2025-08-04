package com.gws.crm.common.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync(proxyTargetClass = true)
@Log
public class AsyncConfig {

    @PostConstruct
    public void testListenerInit() {
        log.info(">>> LeadActionEventListener initialized");
    }

}
