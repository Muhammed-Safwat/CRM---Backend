package com.gws.crm.core.resale.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.AssignDTO;
import com.gws.crm.core.resale.dto.AddResaleDTO;
import com.gws.crm.core.resale.dto.ImportResaleDTO;
import com.gws.crm.core.resale.dto.ResaleCriteria;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResaleService {

    ResponseEntity<?> getResales(@Valid ResaleCriteria resaleCriteria, Transition transition);

    ResponseEntity<?> getResaleDetails(long resaleId, Transition transition);

    ResponseEntity<?> addResale(@Valid AddResaleDTO resaleDTO, Transition transition);

    ResponseEntity<?> updateResale(@Valid AddResaleDTO resaleDTO, Transition transition);

    ResponseEntity<?> deleteResale(Long resaleId, Transition transition);

    ResponseEntity<?> restoreResale(Long resaleId, Transition transition);

    ResponseEntity<?> generateExcel(Transition transition);

    ResponseEntity<?> importResale(@Valid List<ImportResaleDTO> resales, Transition transition);

    ResponseEntity<?> isPhoneExist(List<String> phones, Transition transition);

    ResponseEntity<?> isPhoneExist(String phone, Transition transition);

    ResponseEntity<?> assignSalesToLead(AssignDTO assignDTO, Transition transition);
}
