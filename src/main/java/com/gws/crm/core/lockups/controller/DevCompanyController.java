package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.DevCompanyDTO;
import com.gws.crm.core.lockups.service.DevCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lockups/dev-companies")
@RequiredArgsConstructor
public class DevCompanyController {

    private final DevCompanyService devCompanyService;

    @GetMapping
    public ResponseEntity<?> getDevCompanies(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size, Transition transition) {
        return devCompanyService.getDevCompanies(page, size,transition);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDevCompanies( Transition transition) {
        return devCompanyService.getAllDevCompanies(transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDevCompanyById(@PathVariable long id, Transition transition) {
        return devCompanyService.getDevCompanyById(id, transition);
    }

    @PostMapping
    public ResponseEntity<?> createDevCompany(@Valid @RequestBody DevCompanyDTO devCompany, Transition transition) {
        return devCompanyService.createDevCompany(devCompany, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateDevCompany(@Valid @RequestBody DevCompanyDTO devCompany, Transition transition) {
        return devCompanyService.updateDevCompany(devCompany, transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDevCompany(@PathVariable long id, Transition transition) {
        return devCompanyService.deleteDevCompany(id , transition);
    }

}
