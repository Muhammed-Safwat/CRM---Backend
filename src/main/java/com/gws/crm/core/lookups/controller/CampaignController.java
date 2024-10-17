package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Campaign;
import com.gws.crm.core.lookups.service.impl.CampaignService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lookups/campaigns")
public class CampaignController extends BaseLookupController<Campaign, LookupDTO> {
    protected CampaignController(CampaignService service) {
        super(service);
    }
}
