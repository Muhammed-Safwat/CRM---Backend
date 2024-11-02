package com.gws.crm.core.leads.controller;


import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.service.imp.LeadService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/leads")
@PreAuthorize("hasRole('ADMIN') or hasRole('SALES_ADMIN') or hasRole('SALES_REP')")
public class LeadController extends SalesLeadController<Lead, AddLeadDTO> {

    public LeadController(LeadService service) {
        super(service);
    }

}

