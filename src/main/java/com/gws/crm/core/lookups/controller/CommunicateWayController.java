package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.CommunicateWay;
import com.gws.crm.core.lookups.service.impl.CommunicateWayService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/communicate-ways")
public class CommunicateWayController extends BaseLookupController<CommunicateWay, LookupDTO> {

    protected CommunicateWayController(CommunicateWayService service) {
        super(service);
    }
}
