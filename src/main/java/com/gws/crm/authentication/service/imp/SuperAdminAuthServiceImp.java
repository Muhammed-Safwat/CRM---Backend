package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.constants.RoleName;
import com.gws.crm.authentication.dto.AdminRegistrationDto;
import com.gws.crm.authentication.entity.Role;
import com.gws.crm.authentication.repository.RoleRepository;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.authentication.service.SuperAdminAuthService;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.admin.dto.AdminBasicsInfo;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.mapper.AdminMapper;
import com.gws.crm.core.admin.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.gws.crm.common.handler.ApiResponseHandler.error;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@AllArgsConstructor
public class SuperAdminAuthServiceImp implements SuperAdminAuthService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Override
    public ResponseEntity<?> createAdmin(AdminRegistrationDto adminRegistrationDto, Transition transition) {
        if (userRepository.existsByUsername(adminRegistrationDto.getUsername())) {
            return error("Username is Already exist !");
        }
        Admin admin = initAdmin(adminRegistrationDto);
        adminRepository.save(admin);
        AdminBasicsInfo adminBasicsInfo = adminMapper.toDto(admin);

        return success(adminBasicsInfo, "Admin created successfully");
    }

    private Admin initAdmin(AdminRegistrationDto adminRegistrationDto) {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN_ROLE.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        return Admin.builder()
                .maxNumberOfUsers(adminRegistrationDto.getNumberOfUsers())
                .name(adminRegistrationDto.getName())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .enabled(true)
                .locked(false)
                .credentialsNonExpired(adminRegistrationDto.getAccountExpirationDate())
                .accountNonExpired(adminRegistrationDto.getAccountExpirationDate())
                .image("")
                .roles(roles)
                .username(adminRegistrationDto.getUsername())
                .password(passwordEncoder.encode(adminRegistrationDto.getPassword()))
                .phone(adminRegistrationDto.getPhone())
                .build();
    }

}
