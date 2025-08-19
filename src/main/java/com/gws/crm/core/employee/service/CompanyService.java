package com.gws.crm.core.employee.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.CompanyDTO;
import org.springframework.http.ResponseEntity;


public interface CompanyService {
    ResponseEntity<?> updateCompany(CompanyDTO company, Transition transition) throws Exception;

    ResponseEntity<?> getCompany(Transition transition);
}
