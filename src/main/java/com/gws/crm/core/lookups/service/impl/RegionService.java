package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Region;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;

@Service
public class RegionService extends BaseLookupServiceImpl<Region, LookupDTO> {


    public RegionService(BaseLookupRepository<Region> repository) {
        super(repository);
    }

    @Override
    protected Region mapDtoToEntity(LookupDTO dto, Transition transition) {
        return Region.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(Region entity) {
        return LookupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Region entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
