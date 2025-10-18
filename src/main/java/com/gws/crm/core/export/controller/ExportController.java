package com.gws.crm.core.export.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.export.dtos.ExportCriteria;
import com.gws.crm.core.export.dtos.ExportRequestDto;
import com.gws.crm.core.export.service.ExportOrchestrator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exports")
@AllArgsConstructor
public class ExportController {

    private final ExportOrchestrator orchestrator;

    @PostMapping
    public ResponseEntity<?> export(@RequestBody ExportRequestDto request,  Transition transition) {
        return orchestrator.queueExport(request, transition );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id, Transition transition) {
         return orchestrator.getTask(id,transition);
    }

    @PostMapping("all")
    public ResponseEntity<?> getTasks(@RequestBody ExportCriteria exportCriteria, Transition transition) {
        return orchestrator.getTasks(exportCriteria, transition);
    }

    @GetMapping("statistics")
    public ResponseEntity<?> getStatistics(@RequestParam("referenceType") String referenceType,Transition transition){
        return orchestrator.getStatistics(referenceType,transition);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(@PathVariable Long id, Transition transition) {
        return orchestrator.downloadFile(id, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Transition transition) {
        return orchestrator.deleteFile(id, transition);
    }


}
