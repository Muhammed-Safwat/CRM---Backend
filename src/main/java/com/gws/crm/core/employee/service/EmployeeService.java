package com.gws.crm.core.employee.service;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ChangePasswordDTO;
import com.gws.crm.core.employee.dto.EmployeeCriteria;
import com.gws.crm.core.employee.dto.EmployeeDto;
import com.gws.crm.core.employee.dto.UpdateEmployeeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    ResponseEntity<?> saveEmployee(EmployeeDto employeeDto, Transition transition);

    ResponseEntity<?> getAllEmployee(Transition transition);

    ResponseEntity<?> changePasswordDTO(ChangePasswordDTO changePasswordDTO, Transition transition);

    ResponseEntity<?> updateEmployee(UpdateEmployeeDto employeeDto, Transition transition);

    ResponseEntity<?> toggleLockEmployeeAccount(long employeeId, Transition transition);

    ResponseEntity<?> deleteEmployee(long employeeId, Transition transition);

    ResponseEntity<?> getEmployee(long employeeId, Transition transition);

    ResponseEntity<?> getAllEmployee(EmployeeCriteria employeeCriteria, Transition transition);

    ResponseEntity<?> restoreEmployee(long employeeId, Transition transition);

    ResponseEntity<?> getAllEmployeeType(List<String> types, Transition transition);

    ResponseEntity<?> getSubEmployee(Transition transition);
}
