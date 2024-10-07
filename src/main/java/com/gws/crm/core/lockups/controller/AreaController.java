package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.AreaDTO;
import com.gws.crm.core.lockups.service.AreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lockups/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    @GetMapping
    public ResponseEntity<?> getAreas(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      Transition transition) {
        return areaService.getAreas(page, size, transition);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAreas(Transition transition) {
        return areaService.getAllAreas(transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAreaById(@PathVariable long id, Transition transition) {
        return areaService.getAreaById(id, transition);
    }

    @PostMapping
    public ResponseEntity<?> createArea(@Valid @RequestBody AreaDTO area, Transition transition) {
        return areaService.createArea(area, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateArea(@Valid @RequestBody AreaDTO area, Transition transition) {
        return areaService.updateArea(area, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArea(@PathVariable long id, Transition transition) {
        return areaService.deleteArea(id, transition);
    }

}
