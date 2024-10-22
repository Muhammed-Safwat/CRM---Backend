package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.CallOutcome;
import com.gws.crm.core.lookups.service.impl.CallOutcomeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/call-outcomes")
public class CallOutcomeController extends BaseLookupController<CallOutcome, LookupDTO> {

    protected CallOutcomeController(CallOutcomeService service) {
        super(service);
    }
}
