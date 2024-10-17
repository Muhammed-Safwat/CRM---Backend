package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;

@Service
public class DevCompanyService extends BaseLookupServiceImpl<DevCompany, LookupDTO> {

    public DevCompanyService(BaseLookupRepository<DevCompany> repository) {
        super(repository);
    }

    @Override
    protected DevCompany mapDtoToEntity(LookupDTO dto, Transition transition) {
        return DevCompany.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(DevCompany entity) {
        return LookupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(DevCompany entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
