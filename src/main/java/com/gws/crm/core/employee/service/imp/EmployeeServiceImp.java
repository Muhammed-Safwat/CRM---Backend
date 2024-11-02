package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.constants.RoleName;
import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.authentication.entity.PrivilegeGroup;
import com.gws.crm.authentication.entity.Role;
import com.gws.crm.authentication.repository.PrivilegeRepository;
import com.gws.crm.authentication.repository.RoleRepository;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.employee.dto.*;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.mapper.EmployeeMapper;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.employee.service.EmployeeService;
import com.gws.crm.core.employee.spcification.EmployeeSpecification;
import com.gws.crm.core.leads.repository.PrivilegeGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gws.crm.common.handler.ApiResponseHandler.error;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final AdminRepository adminRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final EmployeeMapper employeeMapper;

    private final PrivilegeGroupRepository privilegeGroupRepository;

    @Transactional
    @Override
    public ResponseEntity<?> saveEmployee(EmployeeDto employeeDto, Transition transition) {
        if (userRepository.existsByUsername(employeeDto.getEmail())) {
            return error("Email is already exist");
        }
        log.info("Transition send {}", transition.getUserId());
        Admin admin = adminRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        if (admin.getMaxNumberOfUsers() <= admin.getEmployees().size()) {
            return error("The maximum number of users for this account has been reached. No additional users can be added.");
        }

        // LocalDateTime accountExpirationDateTime = employeeDto.getAccountExpirationDate().atStartOfDay();

        Set<Privilege> privileges = new HashSet<>(privilegeRepository.findAllById(employeeDto.getPrivileges()));
        PrivilegeGroup jobName = privilegeGroupRepository.getReferenceById(employeeDto.getJobTitleId());
        Role role = roleRepository.findByName(RoleName.USER.toString());
        Role jobNameRole = roleRepository.findByName(jobName.getJobName().replace(" ","_").toUpperCase());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        roles.add(jobNameRole);
        Employee employee = Employee.builder()
                .name(employeeDto.getName())
                .admin(admin)
                .username(employeeDto.getEmail())
                .phone(employeeDto.getPhone())
                .password(passwordEncoder.encode(employeeDto.getPassword()))
                .jobName(jobName.getJobName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .locked(false)
                .accountNonExpired(admin.getAccountNonExpired())
                .credentialsNonExpired(admin.getCredentialsNonExpired())
                .roles(roles)
                .privileges(privileges)
                .build();
        admin.getEmployees().add(employee);
        adminRepository.save(admin);
        EmployeeInfoResponse employeeResponse = employeeMapper.toDto(employee);
        return success(employeeResponse);
    }

    @Override
    public ResponseEntity<?> getAllEmployee(Transition transition) {
        long adminId = transition.getUserId();
        List<Employee> employees = employeeRepository.findAllByAdminId(adminId);
        List<EmployeeSimpleDTO> employeeInfoResponseList = employeeMapper.toListSimpleDto(employees);
        return success(employeeInfoResponseList);
    }

    @Override
    public ResponseEntity<?> changePasswordDTO(ChangePasswordDTO changePasswordDTO, Transition transition) {
        long adminId = transition.getUserId();
        Employee employee = employeeRepository.getByIdAndAdminId(changePasswordDTO.getEmployeeId(), adminId)
                .orElseThrow(NotFoundResourceException::new);
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return error("Passwords not match");
        }
        employee.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        return success("Password changed successfully");
    }

    @Override
    public ResponseEntity<?> updateEmployee(UpdateEmployeeDto employeeDto, Transition transition) {
        long adminId = transition.getUserId();

        Employee employee = employeeRepository.getByIdAndAdminId(employeeDto.getId(), adminId)
                .orElseThrow(NotFoundResourceException::new);

        employee.setName(employeeDto.getName());
        employee.setPhone(employeeDto.getPhone());
        employee.setUsername(employeeDto.getEmail());
        employee.setJobName(privilegeGroupRepository.getReferenceById(employeeDto.getJobTitleId()).getJobName());

        Set<Privilege> privileges = new HashSet<>(privilegeRepository.findAllById(employeeDto.getPrivileges()));
        if (privileges.isEmpty()) {
            throw new IllegalArgumentException("Invalid privileges provided");
        }
        employee.setPrivileges(privileges);

        employeeRepository.save(employee);

        return success("User updated successfully.");
    }

    @Override
    public ResponseEntity<?> toggleLockEmployeeAccount(long employeeId, Transition transition) {
        long adminId = transition.getUserId();

        Employee employee = employeeRepository.getByIdAndAdminId(employeeId, adminId).orElseThrow(NotFoundResourceException::new);

        boolean isCurrentlyLocked = employee.isLocked();
        employee.setLocked(!isCurrentlyLocked);
        employee.setEnabled(!employee.isEnabled());

        employeeRepository.save(employee);

        String status = employee.isLocked() ? "locked" : "unlocked";

        return success("User account has been " + status);
    }

    @Override
    public ResponseEntity<?> deleteEmployee(long employeeId, Transition transition) {
        long adminId = transition.getUserId();
        employeeRepository.deleteByIdAndAdminId(employeeId, adminId);
        return success("User deleted");
    }

    @Override
    public ResponseEntity<?> getEmployee(long employeeId, Transition transition) {
        Employee employee = employeeRepository.getByIdAndAdminId(employeeId, transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        EmployeeInfoResponse employeeResponse = employeeMapper.toDto(employee);

        return success(employeeResponse);
    }

    @Override
    public ResponseEntity<?> getAllEmployee(EmployeeCriteria employeeCriteria, Transition transition) {
        Specification<Employee> spec = EmployeeSpecification.filter(employeeCriteria, transition);
        Pageable pageable = PageRequest.of(employeeCriteria.getPage(), employeeCriteria.getSize());
        Page<Employee> employees = employeeRepository.findAll(spec, pageable);
        List<EmployeeInfoResponse> employeeInfoResponseList = employeeMapper.toListDto(employees.getContent());
        Page<EmployeeInfoResponse> employeeInfoResponsesPage = new PageImpl<>(employeeInfoResponseList, pageable, employees.getTotalElements());
        return success(employeeInfoResponsesPage);
    }

    @Override
    public ResponseEntity<?> restoreEmployee(long employeeId, Transition transition) {
        long adminId = transition.getUserId();
        employeeRepository.restoreByIdAndAdminId(employeeId, adminId);
        return success("User Restored");
    }

}
