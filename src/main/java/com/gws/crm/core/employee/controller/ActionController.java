package com.gws.crm.core.employee.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.employee.service.ActionService;
import com.gws.crm.core.leads.entity.SalesLead;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actions")
public abstract class ActionController<T extends SalesLead> {

    private final ActionService<T> actionService;

    public ActionController(ActionService<T> actionService) {
        this.actionService = actionService;
    }

    @GetMapping("{leadId}")
    public ResponseEntity<?> getActions(@PathVariable long leadId,Transition transition){
        return actionService.getActions(leadId,transition);
    }

    @PostMapping("/lead")
    public ResponseEntity<?> setActionOnLead(@Valid @RequestBody ActionOnLeadDTO action, Transition transition) {
        return actionService.setActionOnLead(action, transition);
    }
}
