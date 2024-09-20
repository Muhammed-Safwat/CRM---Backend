package com.gws.crm.core.employee.mapper;

import com.gws.crm.core.employee.dto.EmployeeInfoResponse;
import com.gws.crm.core.employee.entity.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    EmployeeInfoResponse toDto(Employee employee);

    List<EmployeeInfoResponse> toListDto(List<Employee> employee);

}
