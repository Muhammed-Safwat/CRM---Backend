package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.service.BaseLookupService;
import com.gws.crm.core.lookups.service.impl.DevCompanyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lookups/dev-companies")
public class DevCompanyController  extends  BaseLookupController<DevCompany,LockupDTO> {
    protected DevCompanyController(DevCompanyService service) {
        super(service);
    }
}