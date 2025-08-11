package com.gws.crm.authentication.controller;


import com.gws.crm.authentication.dto.CompanyDTO;
import com.gws.crm.authentication.entity.Company;
import com.gws.crm.authentication.service.CompanyService;
import com.gws.crm.common.entities.Transition;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDTO company, Transition transition) {
        return companyService.updateCompany(company,transition);
    }
    @GetMapping()
    public ResponseEntity<?> getCompany(Transition transition) {
        return companyService.getCompany(transition) ;
    }

}