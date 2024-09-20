package com.gws.crm.core.lockups.controller;

import com.gws.crm.core.lockups.dto.DevCompanyDTO;
import com.gws.crm.core.lockups.service.DevCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dev-companies")
@RequiredArgsConstructor
public class DevCompanyController {

    private final DevCompanyService devCompanyService;

    @GetMapping
    public ResponseEntity<?> getDevCompanies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return devCompanyService.getDevCompanies(page, size);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDevCompanies() {
        return devCompanyService.getAllDevCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDevCompanyById(@PathVariable long id) {
        return devCompanyService.getDevCompanyById(id);
    }

    @PostMapping
    public ResponseEntity<?> createDevCompany(@Valid @RequestBody DevCompanyDTO devCompany) {
        return devCompanyService.createDevCompany(devCompany);
    }

    @PutMapping
    public ResponseEntity<?> updateDevCompany(@Valid @RequestBody DevCompanyDTO devCompany) {
        return devCompanyService.updateDevCompany(devCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevCompany(@PathVariable long id) {
        return devCompanyService.deleteDevCompany(id);
    }

}
