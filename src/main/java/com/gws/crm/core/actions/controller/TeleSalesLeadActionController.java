package com.gws.crm.core.actions.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.dtos.ActionOnLeadDTO;
import com.gws.crm.core.actions.service.imp.TeleSalesLeadActionServiceImp;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actions/telesales-leads")
public class TeleSalesLeadActionController extends ActionController<TeleSalesLead> {

    private final TeleSalesLeadActionServiceImp leadActionService;

    public TeleSalesLeadActionController(TeleSalesLeadActionServiceImp leadActionService) {
        super(leadActionService);
        this.leadActionService = leadActionService;
    }

    @PostMapping("/lead")
    public ResponseEntity<?> setActionOnLead(@Valid @RequestBody ActionOnLeadDTO action, Transition transition) {
        return leadActionService.setActionOnSalesLead(action, transition);
    }
}
