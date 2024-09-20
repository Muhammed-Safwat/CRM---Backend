package com.gws.crm.core.admin.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.admin.service.SuperAdminManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class SuperAdminManagementController {

    private final SuperAdminManagementService superAdminManagementService;

    @GetMapping
    public ResponseEntity<?> getAllAdmins(Transition transition) {
        return superAdminManagementService.getAdmins(transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") long id, Transition transition) {
        return superAdminManagementService.deleteAdmin(id, transition);
    }

}
