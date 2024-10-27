package com.gws.crm.core.admin.controller;

import com.gws.crm.authentication.dto.AdminRegistrationDto;
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

    @GetMapping
    public ResponseEntity<?> getAllAdmins(Transition transition) {
        return superAdminManagementService.getAdmins(transition);
    }

    @GetMapping("{adminId}")
    public ResponseEntity<?> getAdmin(@PathVariable Long adminId, Transition transition) {
        return superAdminManagementService.getAdmin(adminId,transition);
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminRegistrationDto adminRegistrationDto, Transition transition) {
        return superAdminManagementService.createAdmin(adminRegistrationDto, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") long id, Transition transition) {
        return superAdminManagementService.deleteAdmin(id, transition);
    }

    @PutMapping("restore/{id}")
    public ResponseEntity<?> restoreAdmin(@PathVariable("id") long id, Transition transition) {
        return superAdminManagementService.restoreAdmin(id, transition);
    }

    @PutMapping
    public ResponseEntity<?> editAdmin(@Valid @RequestBody AdminRegistrationDto adminRegistrationDto,
                                       Transition transition) {

        return superAdminManagementService.editAdmin(adminRegistrationDto, transition);
    }

    @PutMapping("{adminId}/lock")
    public ResponseEntity<?> toggleAdminLockStatus(@PathVariable long adminId,Transition transition) {
        return superAdminManagementService.toggleAdminLockStatus(adminId,transition);
    }


}
