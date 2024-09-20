package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;


    @GetMapping("all")
    public ResponseEntity<?> getAll() {
        return success(categoryRepository.findAll());
    }


}
