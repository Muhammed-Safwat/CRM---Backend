package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.repository.CompanyRepository;
import com.gws.crm.common.dto.ImageUploadRequest;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.service.ImageHelperService;
import com.gws.crm.core.employee.dto.CompanyDTO;
import com.gws.crm.core.employee.entity.Company;
import com.gws.crm.core.employee.mapper.CompanyMapper;
import com.gws.crm.core.employee.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyServiceImp implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ImageHelperService imageHelperService;
    private final CompanyMapper companyMapper;

    @Override
    public ResponseEntity<?> updateCompany(CompanyDTO companyDTO, Transition transition) throws Exception {
        Company existingCompany = companyRepository.findByAdminId(transition.getUserId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        existingCompany.setName(companyDTO.getName());
        // existingCompany.setLogoUrl(companyDTO.getLogoUrl());
        existingCompany.setEmail(companyDTO.getEmail());
        existingCompany.setPhone(companyDTO.getPhone());
        existingCompany.setWebsite(companyDTO.getWebsite());
        existingCompany.setAddress(companyDTO.getAddress());
        existingCompany.setDescription(companyDTO.getDescription());
        existingCompany.setUpdatedAt(LocalDateTime.now());
        ImageUploadRequest imageUploadReq = ImageUploadRequest.builder()
                .base64Image(companyDTO.getLogoUrl())
                .fileName(null)
                .build();
        if (existingCompany.getLogoUrl() != null && companyDTO.getLogoUrl().startsWith("data:image")) {
            String path = imageHelperService.uploadImage(imageUploadReq, transition);
            if (path == null || path.isEmpty()) {
                imageHelperService.deleteImage(imageHelperService.extractImageIdFromUrl(existingCompany.getLogoUrl()));
            }
            existingCompany.setLogoUrl(path);
        } else if (existingCompany.getLogoUrl() == null && companyDTO.getLogoUrl().startsWith("data:image")) {
            String path = imageHelperService.uploadImage(imageUploadReq, transition);
            existingCompany.setLogoUrl(path);
        }
        companyRepository.save(existingCompany);
        return success(companyMapper.toDto(existingCompany));
    }


    @Override
    public ResponseEntity<?> getCompany(Transition transition) {
        log.info("User id ******>>>>>> {}", transition.getUserId() );
        Company company = companyRepository.findByAdminId(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        return success(companyMapper.toDto(company));
    }
}
