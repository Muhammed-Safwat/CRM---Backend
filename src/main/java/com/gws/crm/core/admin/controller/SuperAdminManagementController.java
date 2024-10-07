package com.gws.crm.core.admin.controller;

import com.gws.crm.authentication.dto.AdminRegistrationDto;
import com.gws.crm.authentication.service.SuperAdminAuthService;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.admin.service.SuperAdminManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin/admins")
@AllArgsConstructor
public class SuperAdminManagementController {

    private final SuperAdminManagementService superAdminManagementService;

    private final SuperAdminAuthService superAdminAuthService;

    @GetMapping
    public ResponseEntity<?> getAllAdmins(Transition transition) {
        return superAdminManagementService.getAdmins(transition);
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminRegistrationDto adminRegistrationDto, Transition transition) {
        return superAdminAuthService.createAdmin(adminRegistrationDto, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") long id, Transition transition) {
        return superAdminManagementService.deleteAdmin(id, transition);
    }

}
