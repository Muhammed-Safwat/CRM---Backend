package com.gws.crm.core.leads.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.LeadCriteria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LeadService {
    ResponseEntity<?> getLeads(int page, int size, Transition transition);

    ResponseEntity<?> getLeadDetails(long leadId, Transition transition);

    ResponseEntity<?> addLead(AddLeadDTO leadDTO, Transition transition);

    ResponseEntity<?> updateLead(AddLeadDTO leadDTO, Transition transition);

    ResponseEntity<?> deleteLead(long leadId, Transition transition);

    ResponseEntity<?> getAllLeads(LeadCriteria leadCriteria, Transition transition);

    ResponseEntity<?> restoreLead(Long leadId, Transition transition);

    ResponseEntity<?> generateExcel(Transition transition);

    ResponseEntity<?> importLead(List<ImportLeadDTO> leads,Transition transition);
}
