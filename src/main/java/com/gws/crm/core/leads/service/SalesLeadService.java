package com.gws.crm.core.leads.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.SalesLeadCriteria;
import com.gws.crm.core.leads.entity.SalesLead;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SalesLeadService<T extends SalesLead, D extends AddLeadDTO> {
    ResponseEntity<?> getLeadDetails(long leadId, Transition transition);

    ResponseEntity<?> addLead(D leadDTO, Transition transition);

    ResponseEntity<?> updateLead(D leadDTO, Transition transition);

    ResponseEntity<?> deleteLead(long leadId, Transition transition);

    ResponseEntity<?> getLeads(SalesLeadCriteria salesLeadCriteria, Transition transition);

    ResponseEntity<?> restoreLead(Long leadId, Transition transition);

    ResponseEntity<?> generateExcel(Transition transition);

    ResponseEntity<?> importLead(List<ImportLeadDTO> leads, Transition transition);
}
