package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.dto.RegionDto;
import com.gws.crm.core.lockups.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<?> getRegions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return regionService.getRegions(page, size);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRegionById(@PathVariable long id) {
        return regionService.getRegionById(id);
    }

    @PostMapping
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionDto region) {
        return regionService.createRegion(region);
    }

    @PutMapping
    public ResponseEntity<?> updateRegion(@RequestBody RegionDto region) {
        return regionService.updateRegion(region);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable long id) {
        return regionService.deleteRegion(id);
    }


}
