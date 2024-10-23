package com.gws.crm.core.employee.controller;

import com.gws.crm.core.employee.service.imp.LeadActionService;
import com.gws.crm.core.leads.entity.Lead;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actions/leads")
public class LeadActionController extends ActionController<Lead> {

    public LeadActionController(LeadActionService leadActionService) {
        super(leadActionService);
    }
}
