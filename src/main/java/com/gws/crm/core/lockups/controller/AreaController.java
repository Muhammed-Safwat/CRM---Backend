package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.dto.AreaDTO;
import com.gws.crm.core.lockups.service.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @GetMapping
    public ResponseEntity<?> getAreas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return areaService.getAreas(page, size);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAreas() {
        return areaService.getAllAreas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAreaById(@PathVariable long id) {
        return areaService.getAreaById(id);
    }

    @PostMapping
    public ResponseEntity<?> createArea(@Valid @RequestBody AreaDTO area) {
        return areaService.createArea(area);
    }

    @PutMapping
    public ResponseEntity<?> updateArea(@Valid @RequestBody AreaDTO area) {
        return areaService.updateArea(area);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArea(@PathVariable long id) {
        return areaService.deleteArea(id);
    }

}
