package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Broker;
import com.gws.crm.core.lookups.service.impl.BrokerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/brokers")
public class BrokerController extends BaseLookupController<Broker, LookupDTO> {

    protected BrokerController(BrokerService service) {
        super(service);
    }
}
