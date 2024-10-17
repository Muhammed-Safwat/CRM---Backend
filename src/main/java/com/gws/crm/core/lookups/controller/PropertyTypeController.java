package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.PropertyTypeDTO;
import com.gws.crm.core.lookups.entity.PropertyType;
import com.gws.crm.core.lookups.service.impl.PropertyTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/lookups/property-type")
public class PropertyTypeController extends BaseLookupController<PropertyType, PropertyTypeDTO> {


    protected PropertyTypeController(PropertyTypeService service) {
        super(service);
    }
}
