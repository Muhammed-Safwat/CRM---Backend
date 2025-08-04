package com.gws.crm.core.actions.controller;


import com.gws.crm.core.actions.service.imp.PreLeadActionServiceImp;
import com.gws.crm.core.leads.entity.PreLead;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/actions/pre-leads")
public class PreLeadActionController extends ActionController<PreLead> {

    public PreLeadActionController(PreLeadActionServiceImp leadActionService) {
        super(leadActionService);
    }

}
