package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.helper.ApiResponse;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.DevCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class DevCompanyService extends BaseLookupServiceImpl<DevCompany , LockupDTO> {

    public DevCompanyService(BaseLookupRepository<DevCompany> repository) {
        super(repository);
    }

    @Override
    protected DevCompany mapDtoToEntity(LockupDTO dto, Transition transition) {
        return null;
    }

    @Override
    protected void updateEntityFromDto(DevCompany entity, LockupDTO dto) {

    }
}
