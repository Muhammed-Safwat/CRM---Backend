package com.gws.crm.core.admin.service.imp;

import com.gws.crm.authentication.constants.RoleName;
import com.gws.crm.authentication.dto.AdminRegistrationDto;
import com.gws.crm.authentication.entity.Company;
import com.gws.crm.authentication.entity.Role;
import com.gws.crm.authentication.repository.RoleRepository;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.dto.AdminBasicsInfo;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.mapper.AdminMapper;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.admin.service.SuperAdminManagementService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SuperAdminManagementServiceImp implements SuperAdminManagementService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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


    @Override
    public ResponseEntity<?> editAdmin(AdminRegistrationDto adminRegistrationDto, Transition transition) {
        Admin admin = adminRepository.findById(adminRegistrationDto.getId())
                .orElseThrow(NotFoundResourceException::new);
        admin.setName(adminRegistrationDto.getName());
        admin.setUsername(adminRegistrationDto.getUsername());
        admin.setPhone(adminRegistrationDto.getPhone());
        admin.setAccountNonExpired(adminRegistrationDto.getAccountExpirationDate());
        admin.setMaxNumberOfUsers(adminRegistrationDto.getNumberOfUsers());
        admin.setUpdatedAt(LocalDateTime.now());
        adminRepository.save(admin);
        AdminBasicsInfo adminBasicsInfo = adminMapper.toDto(admin);
        return success(adminBasicsInfo, "Admin updated successfully");
    }

    @Override
    public ResponseEntity<?> toggleAdminLockStatus(long adminId, Transition transition) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(NotFoundResourceException::new);
        admin.setLocked(!admin.isLocked());
        adminRepository.save(admin);
        AdminBasicsInfo adminBasicsInfo = adminMapper.toDto(admin);
        return success(adminBasicsInfo, "Admin locked successfully");
    }

    @Override
    public ResponseEntity<?> getAdmin(Long adminId, Transition transition) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(NotFoundResourceException::new);
        AdminBasicsInfo adminBasicsInfo = adminMapper.toDto(admin);
        return success(adminBasicsInfo, "Admin found successfully");
    }

    @Override
    public ResponseEntity<?> restoreAdmin(long id, Transition transition) {
        adminRepository.restoreAdmin(id);
        return success("Restored");
    }

    private Admin initAdmin(AdminRegistrationDto adminRegistrationDto) {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        Company company = Company.builder()
                .email(adminRegistrationDto.getCompanyEmail())
                .address(adminRegistrationDto.getCompanyEmail())
                .logoUrl(adminRegistrationDto.getCompanyLogoUrl())
                .website(adminRegistrationDto.getCompanyWebsite())
                .name(adminRegistrationDto.getCompanyName())
                .phone(adminRegistrationDto.getCompanyPhone())
                .build();
        return Admin.builder()
                .maxNumberOfUsers(adminRegistrationDto.getNumberOfUsers())
                .name(adminRegistrationDto.getName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .enabled(true)
                .locked(false)
                .credentialsNonExpired(adminRegistrationDto.getAccountExpirationDate())
                .accountNonExpired(adminRegistrationDto.getAccountExpirationDate())
                .image("")
                .roles(roles)
                .username(adminRegistrationDto.getUsername())
                .password(passwordEncoder.encode(adminRegistrationDto.getPassword()))
                .phone(adminRegistrationDto.getPhone())
                .company(company)
                .build();
    }

    @Override
    public ResponseEntity<?> getAdmins(Transition transition) {
        List<Admin> admins = adminRepository.findAll();
        List<AdminBasicsInfo> adminsBasicsInfo = adminMapper.toDtoList(admins);
        return success(adminsBasicsInfo, "Success");
    }

    @Override
    public ResponseEntity<?> deleteAdmin(long id, Transition transition) {
        adminRepository.deleteAdminById(id);
        return success("Deleted");
    }

}
