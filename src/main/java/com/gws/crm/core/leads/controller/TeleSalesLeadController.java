package com.gws.crm.core.leads.controller;


import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.leads.service.imp.TelesalesLeadService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/telesales-leads")
@PreAuthorize("hasRole('ADMIN') or hasRole('TELESales_AGENT') or hasRole('TELESales_LEADER')")
public class TeleSalesLeadController extends SalesLeadController<TeleSalesLead, AddLeadDTO> {

    public TeleSalesLeadController(TelesalesLeadService service) {
        super(service);
    }
}