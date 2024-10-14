package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.service.BaseLookupService;
import com.gws.crm.core.lookups.service.impl.CancelReasonsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lookups/cancel-reasons")
public class CancelReasonsController extends BaseLookupController<CancelReasons,LockupDTO> {

    protected CancelReasonsController(CancelReasonsService service) {
        super(service);
    }
}
