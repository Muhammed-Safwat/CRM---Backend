package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Region;
import com.gws.crm.core.lookups.service.BaseLookupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lookups/regions")
public class RegionController extends BaseLookupController<Region,LockupDTO> {


    protected RegionController(BaseLookupService<Region, LockupDTO> service) {
        super(service);
    }
}
