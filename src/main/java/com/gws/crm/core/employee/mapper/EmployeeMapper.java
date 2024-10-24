package com.gws.crm.core.employee.mapper;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.employee.dto.EmployeeInfoResponse;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.entity.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    EmployeeInfoResponse toDto(Employee employee);

    List<EmployeeInfoResponse> toListDto(List<Employee> employee);

    List<EmployeeSimpleDTO> toListSimpleDto(List<Employee> employee);

    default EmployeeSimpleDTO toSimpleDto(Employee employee) {

        if (employee == null) {
            return null;
        }

        EmployeeSimpleDTO.EmployeeSimpleDTOBuilder employeeSimpleDTO = EmployeeSimpleDTO.builder();

        employeeSimpleDTO.id(employee.getId());
        employeeSimpleDTO.name(employee.getName());
        employeeSimpleDTO.jobName(employee.getJobName().getJobName());
        employeeSimpleDTO.username(employee.getUsername());
        return employeeSimpleDTO.build();
    }

    default EmployeeSimpleDTO toSimpleDto(User user) {

        if (user == null) {
            return null;
        }

        EmployeeSimpleDTO.EmployeeSimpleDTOBuilder employeeSimpleDTO = EmployeeSimpleDTO.builder();

        employeeSimpleDTO.id(user.getId());
        employeeSimpleDTO.name(user.getName());
        employeeSimpleDTO.image(user.getImage());
        employeeSimpleDTO.username(user.getUsername());

        return employeeSimpleDTO.build();
    }
}
