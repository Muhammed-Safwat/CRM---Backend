package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.dto.ChangePasswordRequest;
import com.gws.crm.authentication.dto.ProfileSettingDTO;
import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.authentication.service.ProfileSettingService;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.mapper.EmployeeMapper;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.gws.crm.common.handler.ApiResponseHandler.error;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@AllArgsConstructor
@Slf4j
public class ProfileSettingServiceImp implements ProfileSettingService {

    private final AdminRepository adminRepository;

    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmployeeMapper employeeMapper;

    @Override
    public ResponseEntity<?> getDetails(Transition transition) {
        ProfileSettingDTO profileSettingDTO = null;
        if (transition.getRole().equals("ADMIN")) {
            Admin admin = adminRepository.findById(transition.getUserId())
                    .orElseThrow(NotFoundResourceException::new);
            profileSettingDTO = ProfileSettingDTO.builder()
                    .id(admin.getId())
                    .email(admin.getUsername())
                    .name(admin.getName())
                    .status(admin.isEnabled() ? "Active": "InActive")
                    .maxUsers((long) admin.getMaxNumberOfUsers())
                    .phoneNumber(admin.getPhone())
                    .numEmployees((long) admin.getEmployees().size())
                    .expirationDate(admin.getAccountNonExpired())
                    .subordinates(employeeMapper.toTeamMemberDto(
                            admin.getEmployees().stream()
                                    .filter(employee -> employee.isEnabled() && !employee.isDeleted() && !employee.isLocked())
                                    .collect(Collectors.toList())
                    ))
                    .build();
        } else {
            Employee employee = employeeRepository.findById(transition.getUserId())
                    .orElseThrow(NotFoundResourceException::new);
            profileSettingDTO = ProfileSettingDTO.builder()
                    .id(employee.getId())
                    .email(employee.getUsername())
                    .name(employee.getName())
                    .status(employee.isEnabled() ? "Active": "InActive")
                    .phoneNumber(employee.getPhone())
                    .expirationDate(employee.getAccountNonExpired())
                    .subordinates(employeeMapper.toTeamMemberDto(employee.getSubordinates()) )
                    .build();
        }
        return success(profileSettingDTO);
    }

    @Override
    public ResponseEntity<?> updateDetails(ProfileSettingDTO profileSettingDTO, Transition transition) {

        if(transition.getRole().equals("ADMIN")){
            Admin admin = adminRepository.findById(transition.getUserId())
                    .orElseThrow(NotFoundResourceException::new);
            admin.setName(profileSettingDTO.getName());
            admin.setPhone(profileSettingDTO.getPhoneNumber());
            log.info(profileSettingDTO.getName());
            log.info(profileSettingDTO.getPhoneNumber());
          adminRepository.save(admin);
        }else {
            Employee employee = employeeRepository.findById(transition.getUserId())
                    .orElseThrow(NotFoundResourceException::new);
            employee.setName(profileSettingDTO.getName());
            employee.setPhone(profileSettingDTO.getPhoneNumber());
            employeeRepository.save(employee);
        }
        return success();
    }


    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest, Transition transition)  {

        User user = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            return  error("Current password is incorrect");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            return  error("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return success("Password changed Successfully");
    }
}
