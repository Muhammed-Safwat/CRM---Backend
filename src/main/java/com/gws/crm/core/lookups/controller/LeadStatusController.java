package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.LeadStatus;
import com.gws.crm.core.lookups.service.impl.LeadStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lookup/statuses")
public class LeadStatusController extends BaseLookupController<LeadStatus , LookupDTO>{

    protected LeadStatusController(LeadStatusService service) {
        super(service);
    }
}
