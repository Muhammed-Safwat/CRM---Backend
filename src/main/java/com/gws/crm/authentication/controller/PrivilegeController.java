package com.gws.crm.authentication.controller;

import com.gws.crm.authentication.service.PrivilegeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/privileges")
@AllArgsConstructor
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @GetMapping("{groupId}")
    public ResponseEntity<?> getPrivileges(@PathVariable long groupId) {
        return privilegeService.getPrivileges(groupId);
    }

    @GetMapping("/groups")
    public ResponseEntity<?> getPrivilegesGroups(){
        return privilegeService.getPrivilegesGroups();
    }


}
