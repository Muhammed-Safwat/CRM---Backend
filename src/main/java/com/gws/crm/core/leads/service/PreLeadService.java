package com.gws.crm.core.leads.service;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AddPreLeadDTO;
import com.gws.crm.core.leads.dto.AssignToSalesDTO;
import com.gws.crm.core.leads.dto.ImportPreLeadDTO;
import com.gws.crm.core.leads.dto.PreLeadCriteria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PreLeadService {
    ResponseEntity<?> getAllPreLead(PreLeadCriteria preLeadCriteria, Transition transition);

    ResponseEntity<?> addPreLead(@Valid AddPreLeadDTO preLeadDTO, Transition transition);

    ResponseEntity<?> deletePreLead(Long leadId, Transition transition);

    ResponseEntity<?> restorePreLead(Long leadId, Transition transition);

    ResponseEntity<?> importLead(@Valid List<ImportPreLeadDTO> leads, Transition transition);

    ResponseEntity<?> generateExcel(Transition transition);

    ResponseEntity<?> assignToSales(@Valid AssignToSalesDTO assignToSalesDTO, Transition transition) throws Throwable;

    ResponseEntity<?> isPhoneExist(List<String> phones, Transition transition);

    ResponseEntity<?> isPhoneExist(String phone, Transition transition);

    ResponseEntity<?> getDetails(long leadId, Transition transition);
}
