package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.PropertyTypeDTO;
import com.gws.crm.core.lookups.entity.PropertyType;
import com.gws.crm.core.lookups.service.impl.PropertyTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/lookups/property-type")
public class PropertyTypeController extends BaseLookupController<PropertyType, PropertyTypeDTO> {

    private final PropertyTypeService service;

    protected PropertyTypeController(PropertyTypeService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getPropertyTypeByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return this.service.getPropertyTypeByCategoryId(categoryId);
    }
}
