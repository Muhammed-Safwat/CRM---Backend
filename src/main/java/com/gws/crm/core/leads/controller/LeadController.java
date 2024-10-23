package com.gws.crm.core.leads.controller;


import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.service.imp.LeadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/leads")
public class LeadController extends SalesLeadController<Lead, AddLeadDTO> {

    public LeadController(LeadService service) {
        super(service);
    }

}

