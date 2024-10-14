package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/lookups/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("all")
    public ResponseEntity<?> getAll(Transition transition) {
        return success(categoryRepository.findAllByAdminId(transition.getUserId()));
    }

}
