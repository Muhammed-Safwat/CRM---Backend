package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.authentication.entity.Role;
import com.gws.crm.authentication.repository.PrivilegeRepository;
import com.gws.crm.authentication.repository.RoleRepository;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeDto;
import com.gws.crm.core.employee.dto.EmployeeInfoResponse;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.mapper.EmployeeMapper;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gws.crm.authentication.constants.RoleName.EMPLOYEE_ROLE;
import static com.gws.crm.common.handler.ApiResponseHandler.error;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final EmployeeMapper employeeMapper;

    @Override
    public ResponseEntity<?> saveEmployee(EmployeeDto employeeDto, Transition transition) {
        if (userRepository.existsByUsername(employeeDto.getEmail())) {
            return error("Email is already exist");
        }
        LocalDateTime accountExpirationDateTime = employeeDto.getAccountExpirationDate().atStartOfDay();
        Role role = roleRepository.findByName(EMPLOYEE_ROLE.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Set<Privilege> privileges = new HashSet<>(privilegeRepository.findAllById(employeeDto.getPrivileges()));
        Employee employee = Employee.builder()
                .name(employeeDto.getName())
                .username(employeeDto.getEmail())
                .phone(employeeDto.getPhone())
                .password(passwordEncoder.encode(employeeDto.getPassword()))
                .jobName(employeeDto.getJobTitle())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .enabled(true)
                .locked(false)
                .accountNonExpired(accountExpirationDateTime)
                .credentialsNonExpired(accountExpirationDateTime)
                .roles(roles)
                .privileges(privileges)
                .build();
        employeeRepository.save(employee);
        EmployeeInfoResponse employeeResponse = employeeMapper.toDto(employee);
        return success(employeeResponse);
    }

    @Override
    public ResponseEntity<?> getAllEmployee(int page, int size, Transition transition) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<EmployeeInfoResponse> employeeInfoResponseList = employeeMapper.toListDto(employees.getContent());
        Page<EmployeeInfoResponse> employeeInfoResponsesPage = new PageImpl<>(
                employeeInfoResponseList,
                pageable,
                employees.getTotalElements()
        );
        return success(employeeInfoResponsesPage);
    }

}
