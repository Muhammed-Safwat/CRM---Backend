package com.gws.crm.authentication.controller;

import com.gws.crm.authentication.dto.AdminRegistrationDto;
import com.gws.crm.authentication.service.SuperAdminAuthService;
import com.gws.crm.common.entities.Transition;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/admin")
@AllArgsConstructor
public class SuperAdminAuthController {

    private final SuperAdminAuthService superAdminAuthService;

    @PostMapping
    //@PreAuthorize("'SUPER_ADMIN_ROLE'")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminRegistrationDto adminRegistrationDto, Transition transition) {
        return superAdminAuthService.createAdmin(adminRegistrationDto, transition);
    }

}
