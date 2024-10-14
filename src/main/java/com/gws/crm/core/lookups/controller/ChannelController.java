package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.service.impl.ChannelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/lookups/channels")
public class ChannelController extends BaseLookupController<Channel, LookupDTO> {

    public ChannelController(ChannelService service) {
        super(service);
    }
}