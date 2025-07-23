package com.gws.crm.core.actions.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.BaseLead;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actions")
public abstract class ActionController<T extends BaseLead> {

    private final LeadActionService<T> leadActionService;

    public ActionController(LeadActionService<T> leadActionService) {
        this.leadActionService = leadActionService;
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<?> getActions(@PathVariable long leadId, Transition transition) {
        return leadActionService.getActions(leadId, transition);
    }

}
