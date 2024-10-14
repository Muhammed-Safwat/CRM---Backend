package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.AreaDTO;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Area;
import com.gws.crm.core.lookups.service.impl.AreaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lookups/areas")
public class AreaController extends BaseLookupController<Area, AreaDTO> {

    protected AreaController(AreaService service) {
        super(service);
    }
}
