package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.service.impl.StageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lookups/stages")
public class StageController extends BaseLookupController<Stage, LookupDTO> {

    protected StageController(StageService service) {
        super(service);
    }
}
