package com.gws.crm.core.lookups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.BaseLookup;
import com.gws.crm.core.lookups.service.BaseLookupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lookups")
public abstract class BaseLookupController<T extends BaseLookup, D extends LookupDTO> {

    private final BaseLookupService<T, D> service;

    protected BaseLookupController(BaseLookupService<T, D> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    Transition transition) {
        return service.getAll(page, size, transition);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAll(Transition transition) {
        return service.getAll(transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, Transition transition) {
        return service.getById(id, transition);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody D dto, Transition transition) {
        return service.create(dto, transition);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody D dto, Transition transition) {
        return service.update(dto, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id, Transition transition) {
        return service.delete(id, transition);
    }
}