package com.gws.crm.core.lockups.service;

import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.lockups.dto.DevCompanyDTO;
import com.gws.crm.core.lockups.entity.DevCompany;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DevCompanyService {

    ResponseEntity<?> getDevCompanies(int page, int size);

    ResponseEntity<ApiResponse<List<DevCompany>>> getAllDevCompanies();

    ResponseEntity<ApiResponse<DevCompany>> getDevCompanyById(long id);

    ResponseEntity<ApiResponse<DevCompany>> createDevCompany(DevCompanyDTO devCompanyDTO);

    ResponseEntity<ApiResponse<DevCompany>> updateDevCompany(DevCompanyDTO devCompanyDTO);

    ResponseEntity<?> deleteDevCompany(long id);
}
