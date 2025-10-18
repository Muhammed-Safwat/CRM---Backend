package com.gws.crm.core.actions.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.dtos.ActionOnLeadDTO;
import com.gws.crm.core.actions.service.LeadActionService;
import com.gws.crm.core.actions.service.imp.SalesLeadActionServiceImp;
import com.gws.crm.core.leads.entity.Lead;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actions/sales-lead")
public class SalesLeadActionController extends ActionController<Lead> {

    private final SalesLeadActionServiceImp leadActionService;

    public SalesLeadActionController(LeadActionService<Lead> leadActionService,
            SalesLeadActionServiceImp salesLeadActionServiceImp) {
        super(leadActionService);
        this.leadActionService = salesLeadActionServiceImp;
    }

    @PostMapping("/lead")
    public ResponseEntity<?> setActionOnLead(@Valid @RequestBody ActionOnLeadDTO action, Transition transition) {
        return leadActionService.setActionOnSalesLead(action, transition);
    }
}
