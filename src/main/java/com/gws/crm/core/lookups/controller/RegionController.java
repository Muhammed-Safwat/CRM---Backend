package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Region;
import com.gws.crm.core.lookups.service.impl.RegionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lookups/regions")
public class RegionController extends BaseLookupController<Region,LookupDTO> {

    protected RegionController(RegionService service) {
        super(service);
    }
}
