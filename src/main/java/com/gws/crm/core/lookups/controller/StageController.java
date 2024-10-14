package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.service.BaseLookupService;
import com.gws.crm.core.lookups.service.impl.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lookups/stages")
public class StageController extends BaseLookupController<Stage, LockupDTO> {

    protected StageController(BaseLookupService<Stage, LockupDTO> service) {
        super(service);
    }
}
