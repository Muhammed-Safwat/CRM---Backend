package com.gws.crm.core.actions.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.service.LeadActionService;
import com.gws.crm.core.leads.entity.BaseLead;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actions")
public abstract class ActionController<T extends BaseLead> {

    private final LeadActionService<T> leadActionService;

    public ActionController(LeadActionService<T> leadActionService) {
        this.leadActionService = leadActionService;
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<?> getActions(
            @PathVariable long leadId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Transition transition) {

        return leadActionService.getActions(leadId, page, size, transition);
    }
}
