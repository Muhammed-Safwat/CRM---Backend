package com.gws.crm;

import com.gws.crm.core.admin.entity.SuperAdmin;
import com.gws.crm.core.admin.repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
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
        SuperAdmin superAdmin = SuperAdmin.builder()
                .name("Muhammed Safwat")
                .username("muhammedsafwat@gmail.com")
                .password(passwordEncoder.encode("superadmin12345"))
                .enabled(true)
                .locked(false)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .accountNonExpired(LocalDateTime.of(9999, 12, 31, 23, 59, 59))
                .credentialsNonExpired(LocalDateTime.of(9999, 12, 31, 23, 59, 59))
                .build();
        superAdminRepository.save(superAdmin);
    }
    */
}
