package com.gws.crm.core.employee.controller;


import com.gws.crm.core.employee.service.LeadActionService;
import com.gws.crm.core.employee.service.imp.PreLeadActionServiceImp;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/actions/pre-leads")
public class PreLeadActionController  extends ActionController<TeleSalesLead> {

    public PreLeadActionController(PreLeadActionServiceImp leadActionService) {
        super(leadActionService);
    }
}
