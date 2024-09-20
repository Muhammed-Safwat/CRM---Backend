package com.gws.crm.core.employee.controller;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeDto;
import com.gws.crm.core.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getAllEmployee(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                            Transition transition) {
        return employeeService.getAllEmployee(page, size, transition);
    }

    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto, Transition transition) {
        return employeeService.saveEmployee(employeeDto, transition);
    }

}
