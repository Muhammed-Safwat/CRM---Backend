package com.gws.crm.core.leads.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddPreLeadDTO;
import com.gws.crm.core.leads.dto.PreLeadCriteria;
import com.gws.crm.core.leads.service.PreLeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leads/pre")
@RequiredArgsConstructor
public class PreLeadController {

    private final PreLeadService preLeadService;

    @PostMapping("all")
    public ResponseEntity<?> getAllPreLead(PreLeadCriteria preLeadCriteria,
                                           Transition transition) {
        return preLeadService.getAllPreLead(preLeadCriteria, transition);
    }


    @PostMapping
    public ResponseEntity<?> addPreLead(@Valid @RequestBody AddPreLeadDTO preLeadDTO,
                                        Transition transition) {
        return preLeadService.addPreLead(preLeadDTO, transition);
    }


}
