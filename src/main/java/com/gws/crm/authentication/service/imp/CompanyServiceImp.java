package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.dto.CompanyDTO;
import com.gws.crm.authentication.entity.Company;
import com.gws.crm.authentication.mapper.CompanyMapper;
import com.gws.crm.authentication.repository.CompanyRepository;
import com.gws.crm.authentication.service.CompanyService;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@RequiredArgsConstructor
@Service
public class CompanyServiceImp implements CompanyService {
    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    @Override
    public ResponseEntity<?> updateCompany(CompanyDTO companyDTO,Transition transition) {
        Company existing = companyRepository.findById(companyDTO.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        existing.setName(companyDTO.getName());
       // existing.setLogoUrl(companyDTO.getLogoUrl());
        existing.setEmail(companyDTO.getEmail());
        existing.setPhone(companyDTO.getPhone());
        existing.setWebsite(companyDTO.getWebsite());
        existing.setAddress(companyDTO.getAddress());
        existing.setDescription(companyDTO.getDescription());
        existing.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(existing);
        return success(companyMapper.toDto(existing));
    }


    @Override
    public ResponseEntity<?> getCompany(Transition transition) {
        Company company = companyRepository.findByAdminId(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        return success(companyMapper.toDto(company));
    }
}
