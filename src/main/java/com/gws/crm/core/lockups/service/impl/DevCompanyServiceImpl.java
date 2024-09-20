package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.lockups.dto.DevCompanyDTO;
import com.gws.crm.core.lockups.entity.DevCompany;
import com.gws.crm.core.lockups.repository.DevCompanyRepository;
import com.gws.crm.core.lockups.service.DevCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class DevCompanyServiceImpl implements DevCompanyService {

    private final DevCompanyRepository devCompanyRepository;

    @Override
    public ResponseEntity<?> getDevCompanies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<DevCompany> devCompanyPage = devCompanyRepository.findAll(pageable);
        return success(devCompanyPage);
    }

    @Override
    public ResponseEntity<ApiResponse<List<DevCompany>>> getAllDevCompanies() {
        List<DevCompany> devCompanies = devCompanyRepository.findAll();
        return success(devCompanies);
    }

    @Override
    public ResponseEntity<ApiResponse<DevCompany>> getDevCompanyById(long id) {
        DevCompany devCompany = devCompanyRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        return success(devCompany);
    }

    @Override
    public ResponseEntity<ApiResponse<DevCompany>> createDevCompany(DevCompanyDTO devCompanyDTO) {
        DevCompany devCompany = DevCompany.builder()
                .name(devCompanyDTO.getName())
                .build();
        DevCompany savedDevCompany = devCompanyRepository.save(devCompany);
        return success(savedDevCompany);
    }

    @Override
    public ResponseEntity<ApiResponse<DevCompany>> updateDevCompany(DevCompanyDTO devCompanyDTO) {
        DevCompany devCompany = devCompanyRepository.findById(devCompanyDTO.getId())
                .orElseThrow(NotFoundResourceException::new);

        devCompany.setName(devCompanyDTO.getName());

        devCompanyRepository.save(devCompany);
        return success(devCompany);
    }

    @Override
    public ResponseEntity<?> deleteDevCompany(long id) {
        devCompanyRepository.deleteById(id);
        return success("Developer Company deleted successfully");
    }
}
