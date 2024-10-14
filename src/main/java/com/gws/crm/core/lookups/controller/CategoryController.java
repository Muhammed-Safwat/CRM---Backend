package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.service.impl.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/categories")
public class CategoryController  extends BaseLookupController<Category, LookupDTO>{

    protected CategoryController(CategoryService service) {
        super(service);
    }
}
