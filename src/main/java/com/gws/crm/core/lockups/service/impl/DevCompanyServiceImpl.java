package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lockups.dto.LockupDTO;
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
    private final AdminRepository adminRepository;
    @Override
    public ResponseEntity<?> getDevCompanies(int page, int size, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<DevCompany> devCompanyPage = devCompanyRepository.findAllByAdminId(pageable, transition.getUserId());
        return success(devCompanyPage);
    }

    @Override
    public ResponseEntity<ApiResponse<List<DevCompany>>> getAllDevCompanies(Transition transition) {
        List<DevCompany> devCompanies = devCompanyRepository.findAll();
        return success(devCompanies);
    }

    @Override
    public ResponseEntity<ApiResponse<DevCompany>> getDevCompanyById(long id, Transition transition) {
        DevCompany devCompany = devCompanyRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        return success(devCompany);
    }

    @Override
    public ResponseEntity<ApiResponse<DevCompany>> createDevCompany(LockupDTO devCompanyDTO, Transition transition) {
        DevCompany devCompany = DevCompany.builder()
                .admin(adminRepository.getReferenceById(transition.getUserId()))
                .name(devCompanyDTO.getName())
                .build();
        DevCompany savedDevCompany = devCompanyRepository.save(devCompany);
        return success(savedDevCompany);
    }

    @Override
    public ResponseEntity<ApiResponse<DevCompany>> updateDevCompany(LockupDTO devCompanyDTO, Transition transition) {
        DevCompany devCompany = devCompanyRepository.findById(devCompanyDTO.getId())
                .orElseThrow(NotFoundResourceException::new);

        devCompany.setName(devCompanyDTO.getName());

        devCompanyRepository.save(devCompany);
        return success(devCompany);
    }

    @Override
    public ResponseEntity<?> deleteDevCompany(long id, Transition transition) {
        devCompanyRepository.deleteById(id);
        return success("Developer Company deleted successfully");
    }
}
