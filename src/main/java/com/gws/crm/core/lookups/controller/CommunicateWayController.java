package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.CommunicateWay;
import com.gws.crm.core.lookups.service.BaseLookupService;
import com.gws.crm.core.lookups.service.impl.CommunicateWayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lookups/communicate-ways")
public class CommunicateWayController extends BaseLookupController<CommunicateWay,LockupDTO>{

    protected CommunicateWayController(CommunicateWayService service) {
        super(service);
    }
}
