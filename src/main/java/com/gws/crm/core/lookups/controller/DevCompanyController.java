package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.service.impl.DevCompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/dev-companies")
public class DevCompanyController extends BaseLookupController<DevCompany, LookupDTO> {
    protected DevCompanyController(DevCompanyService service) {
        super(service);
    }
}