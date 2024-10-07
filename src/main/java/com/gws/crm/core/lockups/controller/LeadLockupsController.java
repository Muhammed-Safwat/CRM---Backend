package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.service.LeadLockupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lead-lockups")
@RequiredArgsConstructor
public class LeadLockupsController {

    private final LeadLockupsService leadLockupsService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getLeadLockups(Transition transition){
        return leadLockupsService.getLeadLockups(transition);
    }

}
