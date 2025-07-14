package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.service.LeadLookupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lead-lookups")
@RequiredArgsConstructor
public class LeadLookupsController {

    private final LeadLookupsService leadLookupsService;

    @GetMapping
    public ResponseEntity<?> getLeadLookups(Transition transition) {
        return leadLookupsService.getLeadLookups(transition);
    }

    @GetMapping("action")
    public ResponseEntity<?> getLeadLookupsAction(Transition transition) {
        return leadLookupsService.getActionLookups(transition);
    }

}
