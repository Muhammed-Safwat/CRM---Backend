package com.gws.crm.core.employee.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.employee.service.LeadActionService;
import com.gws.crm.core.leads.entity.BaseLead;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> getActions(@PathVariable long leadId, Transition transition) {
        return leadActionService.getActions(leadId, transition);
    }

}
