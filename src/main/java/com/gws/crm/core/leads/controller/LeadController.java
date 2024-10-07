package com.gws.crm.core.leads.controller;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.LeadCriteria;
import com.gws.crm.core.leads.service.LeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping
    public ResponseEntity<?> getLeads(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                      Transition transition) {
        return leadService.getLeads(page, size, transition);
    }

    @PostMapping("all")
    public ResponseEntity<?> getAllLeads(@Valid @RequestBody LeadCriteria leadCriteria,
                                         Transition transition) {
        return leadService.getAllLeads(leadCriteria, transition);
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<?> getLeadDetails(@PathVariable("leadId") long leadId, Transition transition) {
        return leadService.getLeadDetails(leadId, transition);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> addLead(@Valid @RequestBody AddLeadDTO leadDTO, Transition transition) {
        return leadService.addLead(leadDTO, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateLead(@Valid @RequestBody AddLeadDTO leadDTO, Transition transition) {
        return leadService.updateLead(leadDTO, transition);
    }

    @DeleteMapping("/{leadId}")
    public ResponseEntity<?> deleteLead(@PathVariable String leadId, Transition transition) {
        return leadService.deleteLead(leadId, transition);
    }
}
