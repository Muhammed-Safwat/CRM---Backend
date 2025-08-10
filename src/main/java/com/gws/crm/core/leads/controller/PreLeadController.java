package com.gws.crm.core.leads.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddPreLeadDTO;
import com.gws.crm.core.leads.dto.AssignToSalesDTO;
import com.gws.crm.core.leads.dto.ImportPreLeadDTO;
import com.gws.crm.core.leads.dto.PreLeadCriteria;
import com.gws.crm.core.leads.service.PreLeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads/pre")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('MARKETING_MANAGER') or hasRole('MARKETING')")
public class PreLeadController {

    private final PreLeadService preLeadService;

    @PostMapping("all")
    public ResponseEntity<?> getAllPreLead(@RequestBody PreLeadCriteria preLeadCriteria,
                                           Transition transition) {
        return preLeadService.getAllPreLead(preLeadCriteria, transition);
    }


    @PostMapping
    public ResponseEntity<?> addPreLead(@Valid @RequestBody AddPreLeadDTO preLeadDTO,
                                        Transition transition) {
        return preLeadService.addPreLead(preLeadDTO, transition);
    }

    @DeleteMapping("/{leadId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLead(@PathVariable Long leadId,
                                        Transition transition) {
        return preLeadService.deletePreLead(leadId, transition);
    }

    @DeleteMapping("restore/{leadId}")
    @PreAuthorize("hasAuthority('ADD_CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> restoreLead(@PathVariable Long leadId,
                                         Transition transition) {
        return preLeadService.restorePreLead(leadId, transition);
    }

    @GetMapping("generate/excel")
    public ResponseEntity<?> generateExcel(Transition transition) {
        return preLeadService.generateExcel(transition);
    }

    @PostMapping("import")
    public ResponseEntity<?> importLead(@Valid @RequestBody List<ImportPreLeadDTO> leads,
                                        Transition transition) {
        return preLeadService.importLead(leads, transition);
    }

    @PostMapping("/assignToSales")
    public ResponseEntity<?> assignToSales(@Valid @RequestBody AssignToSalesDTO assignToSalesDTO,
                                           Transition transition) throws Throwable {
        return preLeadService.assignToSales(assignToSalesDTO, transition);
    }

    @GetMapping("/is-phone-exist/{phone}")
    public ResponseEntity<?> isPhoneExist(@PathVariable String phone, Transition transition) {
        return preLeadService.isPhoneExist(phone, transition);
    }

    @PostMapping("/is-phone-exist")
    public ResponseEntity<?> isPhoneExist(@RequestBody List<String> phones, Transition transition) {
        return preLeadService.isPhoneExist(phones, transition);
    }

    @GetMapping("details/{leadId}")
    public ResponseEntity<?> getDetails(@PathVariable long leadId, Transition transition) {
        return preLeadService.getDetails(leadId, transition);
    }

    @PutMapping("archive/{leadId}")
    public ResponseEntity<?> toggleArchive(@PathVariable long leadId, Transition transition) {
        return preLeadService.toggleArchive(leadId, transition);
    }
}
