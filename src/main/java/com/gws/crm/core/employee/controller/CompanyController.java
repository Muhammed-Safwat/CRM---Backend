package com.gws.crm.core.employee.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.CompanyDTO;
import com.gws.crm.core.employee.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PutMapping
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDTO company, Transition transition) throws Exception {
        return companyService.updateCompany(company, transition);
    }

    @GetMapping
    public ResponseEntity<?> getCompany(Transition transition) {
        return companyService.getCompany(transition);
    }

}