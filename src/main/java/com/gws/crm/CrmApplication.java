package com.gws.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CrmApplication {
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
