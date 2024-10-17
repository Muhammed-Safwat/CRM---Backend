package com.gws.crm.core.leads.service;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddPreLeadDTO;
import com.gws.crm.core.leads.dto.ImportPreLeadDTO;
import com.gws.crm.core.leads.dto.PreLeadCriteria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PreLeadService {
    ResponseEntity<?> getAllPreLead(PreLeadCriteria preLeadCriteria, Transition transition);

    ResponseEntity<?> addPreLead(@Valid AddPreLeadDTO preLeadDTO, Transition transition);

    ResponseEntity<?> deletePreLead(Long leadId, Transition transition);

    ResponseEntity<?> restorePreLead(Long leadId, Transition transition);

    ResponseEntity<?> importLead(@Valid List<ImportPreLeadDTO> leads, Transition transition);

    ResponseEntity<?> generateExcel(Transition transition);
}
