package com.gws.crm.core.admin.service.imp;

import com.gws.crm.authentication.constants.RoleName;
import com.gws.crm.authentication.dto.AdminRegistrationDto;
import com.gws.crm.authentication.entity.Company;
import com.gws.crm.authentication.entity.Role;
import com.gws.crm.authentication.repository.CompanyRepository;
import com.gws.crm.authentication.repository.RoleRepository;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.dto.ImageUploadRequest;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.service.ImageHelperService;
import com.gws.crm.core.admin.dto.AdminBasicsInfo;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.admin.mapper.AdminMapper;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.admin.service.SuperAdminManagementService;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.spec.ECField;
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
    private final CompanyRepository companyRepository;
    private final ImageHelperService imageHelperService;

    @Override
    public ResponseEntity<?> createAdmin(AdminRegistrationDto adminRegistrationDto, Transition transition) {
        if (userRepository.existsByUsername(adminRegistrationDto.getUsername())) {
            return error("Username is Already exist !");
        }
        Admin admin = initAdmin(adminRegistrationDto,transition);
        adminRepository.save(admin);
        Company company = admin.getCompany();
        company.setAdmin(admin);
        companyRepository.save(company);
        AdminBasicsInfo adminBasicsInfo = adminMapper.toDto(admin);

        return success(adminBasicsInfo, "Admin created successfully");
    }

    @Override
    public ResponseEntity<?> editAdmin(AdminRegistrationDto adminDto, Transition transition) throws Exception {
        Admin admin = adminRepository.findById(adminDto.getId())
                .orElseThrow(NotFoundResourceException::new);
        admin.setName(adminDto.getName());
        admin.setUsername(adminDto.getUsername());
        admin.setPhone(adminDto.getPhone());
        admin.setAccountNonExpired(adminDto.getAccountExpirationDate());
        admin.setCredentialsNonExpired(adminDto.getAccountExpirationDate());
        admin.setMaxNumberOfUsers(adminDto.getNumberOfUsers());
        admin.setUpdatedAt(LocalDateTime.now());
        Company company = admin.getCompany();
        if(admin.getCompany() == null){
            company = new Company();
            admin.setCompany(company);
        }
        company.setName(adminDto.getCompanyName());
        company.setPhone(adminDto.getCompanyPhone());
        company.setEmail(adminDto.getCompanyEmail());
        company.setUpdatedAt(LocalDateTime.now());
        company.setDescription(adminDto.getCompanyDescription());
        company.setWebsite(adminDto.getCompanyWebsite());
        ImageUploadRequest imageUploadReq =  ImageUploadRequest.builder()
                .base64Image(adminDto.getCompanyLogo())
                .fileName(null)
                .build();
        if(admin.getCompany().getLogoUrl() != null && adminDto.getCompanyLogo().startsWith("data:image")){
            String path = imageHelperService.uploadImage(imageUploadReq,transition);
            if (path == null || path.isEmpty()) {
                imageHelperService.deleteImage(imageHelperService.extractImageIdFromUrl(admin.getCompany().getLogoUrl()));
            }
            company.setLogoUrl(path);
        }else  if(admin.getCompany().getLogoUrl() == null && adminDto.getCompanyLogo().startsWith("data:image")){
            String path = imageHelperService.uploadImage(imageUploadReq,transition);
            company.setLogoUrl(path);
        }
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

    private Admin initAdmin(AdminRegistrationDto adminRegistrationDto,Transition transition) {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN.toString());
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        ImageUploadRequest imageUploadReq =  ImageUploadRequest.builder()
                .base64Image(adminRegistrationDto.getCompanyLogo()).fileName(null).build();
        String companyUrl = null ;
        try{
            companyUrl = imageHelperService.uploadImage(imageUploadReq,transition);

         }catch (Exception e){
             throw new RuntimeException(e.getMessage());
         }
        Company company = Company.builder()
                .email(adminRegistrationDto.getCompanyEmail())
                .address(adminRegistrationDto.getCompanyAddress())
                .logoUrl(companyUrl)
                .website(adminRegistrationDto.getCompanyWebsite())
                .name(adminRegistrationDto.getCompanyName())
                .phone(adminRegistrationDto.getCompanyPhone())
                .createdAt(LocalDateTime.now())
                .description(adminRegistrationDto.getCompanyDescription())
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
