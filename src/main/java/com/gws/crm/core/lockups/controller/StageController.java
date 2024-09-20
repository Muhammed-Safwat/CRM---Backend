package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.dto.StageDto;
import com.gws.crm.core.lockups.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stages")
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @GetMapping
    public ResponseEntity<?> getStage(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return stageService.getStage(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStageById(@PathVariable long id) {
        return stageService.getStageById(id);
    }

    @PostMapping
    public ResponseEntity<?> createStage(@RequestBody StageDto stageDto) {
        return stageService.createStage(stageDto);
    }

    @PutMapping()
    public ResponseEntity<?> updateStage(@RequestBody StageDto stageDto) {
        return stageService.updateStage(stageDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStage(@PathVariable String id) {
        return stageService.deleteStage(Long.parseLong(id));
    }

}
