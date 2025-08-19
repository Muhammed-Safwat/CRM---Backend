package com.gws.crm.core.employee.mapper;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.employee.dto.EmployeeInfoResponse;
import com.gws.crm.core.employee.dto.EmployeeResponse;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.dto.EmployeeTeamMemberDto;
import com.gws.crm.core.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface EmployeeMapper {

    // Mapping Employee to EmployeeInfoResponse with mapping of subordinates
    @Mapping(source = "subordinates", target = "subordinates", qualifiedByName = "mapSubordinates")
    EmployeeInfoResponse toDto(Employee employee);

    EmployeeResponse toEmployeeResponseDto(Employee employee);

    EmployeeTeamMemberDto toTeamMemberDto(Employee employee);

    List<EmployeeTeamMemberDto> toTeamMemberDto(Set<Employee> employees);

    List<EmployeeTeamMemberDto> toTeamMemberDto(List<Employee> employees);

    List<EmployeeInfoResponse> toListDto(List<Employee> employees);

    List<EmployeeSimpleDTO> toListSimpleDto(Set<Employee> employees);


    default EmployeeSimpleDTO toSimpleDto(Employee employee) {
        if (employee == null) {
            return null;
        }

        return EmployeeSimpleDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .jobName(employee.getJobName())
                .username(employee.getUsername())
                .build();
    }

    default EmployeeSimpleDTO toSimpleDto(User user) {
        if (user == null) {
            return null;
        }

        return EmployeeSimpleDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .image(user.getImage())
                .username(user.getUsername())
                .build();
    }

    @Named("mapSubordinates")
    default List<EmployeeSimpleDTO> mapSubordinates(Set<Employee> subordinates) {
        return subordinates.stream()
                .map(this::toSimpleDto)
                .collect(Collectors.toList());
    }

    default List<EmployeeResponse> toEmployeeResponseList(List<Employee> employees) {
        if ( employees == null ) {
            return null;
        }

        List<EmployeeResponse> list = new ArrayList<>( employees.size() );
        for ( Employee employee : employees ) {
            list.add( toEmployeeResponseDto( employee ) );
        }

        return list;
    }
}
