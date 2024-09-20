package com.gws.crm.core.employee.service;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {

    ResponseEntity<?> saveEmployee(EmployeeDto employeeDto, Transition transition);

    ResponseEntity<?> getAllEmployee(int page, int size, Transition transition);
}
