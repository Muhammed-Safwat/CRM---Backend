package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.AreaDTO;
import com.gws.crm.core.lookups.entity.Area;
import com.gws.crm.core.lookups.entity.Region;
import com.gws.crm.core.lookups.repository.AreaRepository;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class AreaService extends BaseLookupServiceImpl<Area,AreaDTO> {

    @Autowired
    private RegionRepository regionRepository;

    public AreaService(AreaRepository repository) {
        super(repository);
    }

    @Override
    protected Area mapDtoToEntity(AreaDTO dto, Transition transition) {
        return Area.builder()
                .name(dto.getName())
                .region(regionRepository.getReferenceById(dto.getRegion().getId()))
                .build();
    }

    @Override
    protected void updateEntityFromDto(Area entity, AreaDTO dto) {
        entity.setRegion(regionRepository.getReferenceById(dto.getRegion().getId()));
        entity.setName(dto.getName());
    }
}
