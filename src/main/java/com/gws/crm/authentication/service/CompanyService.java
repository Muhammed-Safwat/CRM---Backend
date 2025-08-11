package com.gws.crm.authentication.service;

import com.gws.crm.authentication.dto.CompanyDTO;
import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;


public interface CompanyService {
    ResponseEntity<?>  updateCompany(CompanyDTO company, Transition transition);
    ResponseEntity<?> getCompany (Transition transition);
}
