package com.gws.crm.core.resale.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.resale.dto.AddResaleDTO;
import com.gws.crm.core.resale.dto.ImportResaleDTO;
import com.gws.crm.core.resale.dto.ResaleCriteria;
import com.gws.crm.core.resale.service.ResaleLookupsService;
import com.gws.crm.core.resale.service.ResaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resales")
@RequiredArgsConstructor
public class ResaleController {

    private final ResaleService resaleService;
    private final ResaleLookupsService resaleLookupsService;

    @PostMapping("all")
    public ResponseEntity<?> getResales(@Valid @RequestBody ResaleCriteria resaleCriteria,
                                        Transition transition) {
        return resaleService.getResales(resaleCriteria, transition);
    }

    @GetMapping("/{resaleId}")
    public ResponseEntity<?> getResaleDetails(@Valid @PathVariable("resaleId") long resaleId,
                                              Transition transition) {
        return resaleService.getResaleDetails(resaleId, transition);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> addResale(@Valid @RequestBody AddResaleDTO resaleDTO,
                                       Transition transition) {
        return resaleService.addResale(resaleDTO, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateResale(@Valid @RequestBody AddResaleDTO resaleDTO,
                                          Transition transition) {
        return resaleService.updateResale(resaleDTO, transition);
    }

    @DeleteMapping("/{resaleId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteResale(@PathVariable Long resaleId,
                                          Transition transition) {
        return resaleService.deleteResale(resaleId, transition);
    }

    @DeleteMapping("restore/{resaleId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> restoreResale(@PathVariable Long resaleId,
                                           Transition transition) {
        return resaleService.restoreResale(resaleId, transition);
    }

    @GetMapping("generate/excel")
    public ResponseEntity<?> generateExcel(Transition transition) {
        return resaleService.generateExcel(transition);
    }

    @PostMapping("import")
    public ResponseEntity<?> importResale(@Valid @RequestBody List<ImportResaleDTO> resales,
                                          Transition transition) {
        return resaleService.importResale(resales, transition);
    }

    @GetMapping("lookups")
    public ResponseEntity<?> getLookups(Transition transition) {
        return resaleLookupsService.getLookups(transition);
    }

}
