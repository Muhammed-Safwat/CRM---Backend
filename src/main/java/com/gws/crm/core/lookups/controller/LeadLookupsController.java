package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.service.LeadLookupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lead-lookups")
@RequiredArgsConstructor
public class LeadLookupsController {

    private final LeadLookupsService leadLookupsService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getLeadLookups(Transition transition) {
        return leadLookupsService.getLeadLookups(transition);
    }

    @GetMapping("action")
    public ResponseEntity<?> getLeadLookupsAction(Transition transition) {
        return leadLookupsService.getActionLookups(transition);
    }

}
