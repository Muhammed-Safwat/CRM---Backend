package com.gws.crm.core.resale.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.resale.dto.AddResaleDTO;
import com.gws.crm.core.resale.dto.ImportResaleDTO;
import com.gws.crm.core.resale.dto.ResaleCriteria;
import com.gws.crm.core.resale.service.ResaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resale")
@RequiredArgsConstructor
public class ResaleController {

    private final ResaleService resaleService;

    @GetMapping
    public ResponseEntity<?> getResales(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                        Transition transition) {
        return resaleService.getResales(page, size, transition);
    }

    @PostMapping("all")
    public ResponseEntity<?> getAllResales(@Valid @RequestBody ResaleCriteria resaleCriteria,
                                           Transition transition) {
        return resaleService.getAllResales(resaleCriteria, transition);
    }

    @GetMapping("/{resaleId}")
    public ResponseEntity<?> getResaleDetails(@PathVariable("resaleId") long resaleId,
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


}
