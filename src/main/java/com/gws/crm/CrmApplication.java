package com.gws.crm;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@Log
@EnableScheduling
@EnableAspectJAutoProxy
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CrmApplication {

   /* @Async
    @EventListener
    public void testAsync(TestEvent event) {
        log.info("🧵 Current Thread: " + Thread.currentThread().getName());
    }*/

    /*
        @Autowired
        private SuperAdminRepository superAdminRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;
    */

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

    /*
    @Override
    public void run(String... args) throws Exception {

    }
    */
}
