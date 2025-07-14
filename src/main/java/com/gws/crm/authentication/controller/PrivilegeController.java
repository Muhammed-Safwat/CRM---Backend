package com.gws.crm.authentication.controller;

import com.gws.crm.authentication.dto.PrivilegeGroupCriteria;
import com.gws.crm.authentication.service.PrivilegeService;
import com.gws.crm.common.entities.Transition;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getPrivilegesGroups() {
        return privilegeService.getPrivilegesGroups();
    }

    @PostMapping("/groups")
    public ResponseEntity<?> getPrivileges(@RequestBody PrivilegeGroupCriteria privilegeGroupCriteria, Transition transition) {
        return privilegeService.getPrivileges(privilegeGroupCriteria, transition);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<?> getDetails(@PathVariable Long id, Transition transition) {
        return privilegeService.getGroupDetails(id, transition);
    }

}
