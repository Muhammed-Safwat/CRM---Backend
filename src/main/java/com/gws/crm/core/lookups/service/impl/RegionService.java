package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Region;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class RegionService extends BaseLookupServiceImpl<Region, LockupDTO> {


    public RegionService(BaseLookupRepository<Region> repository) {
        super(repository);
    }

    @Override
    protected Region mapDtoToEntity(LockupDTO dto, Transition transition) {
        return Region.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Region entity, LockupDTO dto) {
        entity.setName(dto.getName());
    }
}
