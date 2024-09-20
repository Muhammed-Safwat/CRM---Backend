package com.gws.crm.authentication.controller;

import com.gws.crm.authentication.service.PrivilegeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/privileges")
@AllArgsConstructor
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @GetMapping
    public ResponseEntity<?> getPrivileges() {
        return privilegeService.getPrivileges();
    }


}
