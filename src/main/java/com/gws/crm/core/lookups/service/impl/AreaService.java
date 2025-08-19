package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.AreaDTO;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Area;
import com.gws.crm.core.lookups.repository.AreaRepository;
import com.gws.crm.core.lookups.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaService extends BaseLookupServiceImpl<Area, AreaDTO> {

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private AdminRepository adminRepository;

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
    protected AreaDTO mapEntityToDto(Area entity) {
        return AreaDTO.builder()
                .name(entity.getName())
                .id(entity.getId())
                .region(LookupDTO.builder()
                        .name(entity.getName())
                        .id(entity.getId())
                        .build())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Area entity, AreaDTO dto) {
        entity.setRegion(regionRepository.getReferenceById(dto.getRegion().getId()));
        entity.setName(dto.getName());
    }
}