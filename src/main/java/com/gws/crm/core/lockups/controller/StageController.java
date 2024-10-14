package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.LockupDTO;
import com.gws.crm.core.lockups.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lockups/stages")
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @GetMapping
    public ResponseEntity<?> getStage(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size, Transition transition) {
        return stageService.getStage(page, size, transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStageById(@PathVariable long id, Transition transition) {
        return stageService.getStageById(id, transition);
    }

    @PostMapping
    public ResponseEntity<?> createStage(@RequestBody LockupDTO stageDto, Transition transition) {
        return stageService.createStage(stageDto, transition);
    }

    @PutMapping()
    public ResponseEntity<?> updateStage(@RequestBody LockupDTO stageDto, Transition transition) {
        return stageService.updateStage(stageDto, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStage(@PathVariable long id, Transition transition) {
        return stageService.deleteStage(id, transition);
    }

}
