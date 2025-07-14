package com.gws.crm.core.employee.controller;

import com.gws.crm.core.employee.service.imp.TeleSalesLeadActionServiceImp;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actions/telesales-leads")
public class TeleSalesLeadActionController extends ActionController<TeleSalesLead> {

    public TeleSalesLeadActionController(TeleSalesLeadActionServiceImp leadActionService) {
        super(leadActionService);
    }
}
