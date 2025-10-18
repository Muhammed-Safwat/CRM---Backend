package com.gws.crm.core.employee.controller;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ChangePasswordDTO;
import com.gws.crm.core.employee.dto.EmployeeCriteria;
import com.gws.crm.core.employee.dto.EmployeeDto;
import com.gws.crm.core.employee.dto.UpdateEmployeeDto;
import com.gws.crm.core.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getAllEmployee(Transition transition) {
        return employeeService.getAllEmployee(transition);
    }

    @GetMapping("sub")
    public ResponseEntity<?> getSubEmployee(Transition transition) {
        return employeeService.getSubEmployee(transition);
    }

    @PostMapping("/type")
    public ResponseEntity<?> getAllEmployeeType(@RequestBody List<String> types, Transition transition) {
        return employeeService.getAllEmployeeInType(types, transition);
    }

    @PostMapping("all")
    public ResponseEntity<?> getAllEmployee(@RequestBody @Valid EmployeeCriteria employeeCriteria,
                                            Transition transition) {
        return employeeService.getAllEmployee(employeeCriteria, transition);
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<?> getEmployee(@PathVariable long employeeId, Transition transition) {
        return employeeService.getEmployee(employeeId, transition);
    }

    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto, Transition transition) {
        return employeeService.saveEmployee(employeeDto, transition);
    }

    @PutMapping
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody UpdateEmployeeDto employeeDto, Transition transition) {
        return employeeService.updateEmployee(employeeDto, transition);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changeEmployeePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, Transition transition) {
        return employeeService.changePasswordDTO(changePasswordDTO, transition);
    }

    @PutMapping("/{employeeId}/lock-account")
    public ResponseEntity<?> toggleLockEmployeeAccount(@PathVariable long employeeId, Transition transition) {
        return employeeService.toggleLockEmployeeAccount(employeeId, transition);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable long employeeId, Transition transition) {
        return employeeService.deleteEmployee(employeeId, transition);
    }

    @PutMapping("restore/{employeeId}")
    public ResponseEntity<?> restoreEmployee(@PathVariable long employeeId, Transition transition) {
        return employeeService.restoreEmployee(employeeId, transition);
    }

}