package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.RegionDto;
import com.gws.crm.core.lockups.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lockups/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<?> getRegions(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size, Transition transition) {
        return regionService.getRegions(page, size, transition);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllRegions(Transition transition) {
        return regionService.getAllRegions( transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRegionById(@PathVariable long id, Transition transition) {
        return regionService.getRegionById(id, transition);
    }

    @PostMapping
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionDto region, Transition transition) {
        return regionService.createRegion(region, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateRegion(@RequestBody RegionDto region, Transition transition) {
        return regionService.updateRegion(region, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable long id, Transition transition) {
        return regionService.deleteRegion(id, transition);
    }


}
