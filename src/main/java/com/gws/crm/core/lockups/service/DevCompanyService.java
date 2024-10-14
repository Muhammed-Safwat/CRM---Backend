package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.lockups.dto.LockupDTO;
import com.gws.crm.core.lockups.entity.DevCompany;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DevCompanyService {

    ResponseEntity<?> getDevCompanies(int page, int size, Transition transition);

    ResponseEntity<ApiResponse<List<DevCompany>>> getAllDevCompanies(Transition transition);

    ResponseEntity<ApiResponse<DevCompany>> getDevCompanyById(long id, Transition transition);

    ResponseEntity<ApiResponse<DevCompany>> createDevCompany(LockupDTO devCompanyDTO, Transition transition);

    ResponseEntity<ApiResponse<DevCompany>> updateDevCompany(LockupDTO devCompanyDTO, Transition transition);

    ResponseEntity<?> deleteDevCompany(long id, Transition transition);
}
