package com.gws.crm.core.leads.controller;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.LeadCriteria;
import com.gws.crm.core.leads.service.LeadService;
import com.gws.crm.core.leads.service.TeleSalesLeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/telesales-leads")
@RequiredArgsConstructor
@Log
public class TeleSalesLeadController {

    private final TeleSalesLeadService teleSalesLeadService;

    @PostMapping("all")
    public ResponseEntity<?> getAllLeads(@Valid @RequestBody LeadCriteria leadCriteria,
                                         Transition transition) {
        return teleSalesLeadService.getLeads(leadCriteria, transition);
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<?> getLeadDetails(@PathVariable("leadId") long leadId,
                                            Transition transition) {
        return teleSalesLeadService.getLeadDetails(leadId, transition);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> addLead(@Valid @RequestBody AddLeadDTO leadDTO,
                                     Transition transition) {
        return teleSalesLeadService.addLead(leadDTO, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateLead(@Valid @RequestBody AddLeadDTO leadDTO,
                                        Transition transition) {
        return teleSalesLeadService.updateLead(leadDTO, transition);
    }

    @DeleteMapping("/{leadId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLead(@PathVariable Long leadId,
                                        Transition transition) {
        return teleSalesLeadService.deleteLead(leadId, transition);
    }

    @DeleteMapping("restore/{leadId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> restoreLead(@PathVariable Long leadId,
                                         Transition transition) {
        return teleSalesLeadService.restoreLead(leadId, transition);
    }

    @GetMapping("generate/excel")
    public ResponseEntity<?> generateExcel(Transition transition) {
        return teleSalesLeadService.generateExcel(transition);
    }

    @PostMapping("import")
    public ResponseEntity<?> importLead(@Valid @RequestBody List<ImportLeadDTO> leads,
                                        Transition transition) {
        return teleSalesLeadService.importLead(leads, transition);
    }

}
