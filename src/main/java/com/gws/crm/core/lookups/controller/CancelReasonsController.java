package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.service.impl.CancelReasonsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/cancel-reasons")
public class CancelReasonsController extends BaseLookupController<CancelReasons,LookupDTO> {

    protected CancelReasonsController(CancelReasonsService service) {
        super(service);
    }
}
