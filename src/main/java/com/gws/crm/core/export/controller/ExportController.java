package com.gws.crm.core.export.controller;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.export.dtos.ExportCritira;
import com.gws.crm.core.export.dtos.ExportRequestDto;
import com.gws.crm.core.export.dtos.ExportResponseDto;
import com.gws.crm.core.export.entity.ExportTask;
import com.gws.crm.core.export.mapper.ExportTaskMapper;
import com.gws.crm.core.export.service.ExportOrchestrator;
import com.gws.crm.core.export.spcification.ExportSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    public ResponseEntity<?> getTasks(@RequestBody ExportCritira exportCritira, Transition transition) {
        return orchestrator.getTasks(exportCritira, transition);
    }

}
