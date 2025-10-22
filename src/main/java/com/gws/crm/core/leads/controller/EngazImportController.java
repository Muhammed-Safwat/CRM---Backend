package com.gws.crm.core.leads.controller;


import com.gws.crm.core.leads.dto.EngazLeadDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("import")
public class EngazImportController {

    @PostMapping("engaz-leads")
    public ResponseEntity<?> importLeads(@RequestBody List<EngazLeadDto> leads) {
        leads.forEach(lead -> {
            System.out.println("Lead: " + lead.getFullName());
            lead.getActions().forEach(a ->
                    System.out.println(" - Action: " + a.getStage() + " / " + a.getComment()));
        });
        return ResponseEntity.ok("Imported successfully");
    }

}
