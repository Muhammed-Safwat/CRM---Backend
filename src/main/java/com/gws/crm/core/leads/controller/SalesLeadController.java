package com.gws.crm.core.leads.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.AssignDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.SalesLeadCriteria;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.service.SalesLeadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public abstract class SalesLeadController<T extends SalesLead, D extends AddLeadDTO> {

    private final SalesLeadService<T, D> service;

    public SalesLeadController(SalesLeadService<T, D> service) {
        this.service = service;
    }


    @PostMapping("all")
    public ResponseEntity<?> getAllLeads(@Valid @RequestBody SalesLeadCriteria salesLeadCriteria,
                                         Transition transition) {
        return service.getLeads(salesLeadCriteria, transition);
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<?> getLeadDetails(@PathVariable("leadId") long leadId,
                                            Transition transition) {
        return service.getLeadDetails(leadId, transition);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> addLead(@Valid @RequestBody D leadDTO,
                                     Transition transition) {
        return service.addLead(leadDTO, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateLead(@Valid @RequestBody D leadDTO,
                                        Transition transition) {
        return service.updateLead(leadDTO, transition);
    }

    @DeleteMapping("/{leadId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLead(@PathVariable Long leadId,
                                        Transition transition) {
        return service.deleteLead(leadId, transition);
    }

    @DeleteMapping("restore/{leadId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> restoreLead(@PathVariable Long leadId,
                                         Transition transition) {
        return service.restoreLead(leadId, transition);
    }

    @GetMapping("generate/excel")
    public ResponseEntity<?> generateExcel(Transition transition) {
        return service.generateExcel(transition);
    }

    @PostMapping("import")
    public ResponseEntity<?> importLead(@Valid @RequestBody List<ImportLeadDTO> leads,
                                        Transition transition) {
        return service.importLead(leads, transition);
    }

    @PutMapping("/assign")
    public ResponseEntity<?> assignSalesToLead(@RequestBody AssignDTO assignDTO, Transition transition) {
        return service.assignSalesToLead(assignDTO, transition);
    }

}
