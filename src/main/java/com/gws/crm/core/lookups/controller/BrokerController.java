package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Broker;
import com.gws.crm.core.lookups.service.BaseLookupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/brokers")
public class BrokerController  extends BaseLookupController<Broker, LockupDTO> {

    protected BrokerController(BaseLookupService<Broker, LockupDTO> service) {
        super(service);
    }
}
