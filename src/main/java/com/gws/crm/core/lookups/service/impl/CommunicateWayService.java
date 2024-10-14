package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.CommunicateWay;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;

@Service
public class CommunicateWayService  extends BaseLookupServiceImpl<CommunicateWay,LookupDTO> {

    public CommunicateWayService(BaseLookupRepository<CommunicateWay> repository) {
        super(repository);
    }

    @Override
    protected CommunicateWay mapDtoToEntity(LookupDTO dto, Transition transition) {
        return CommunicateWay.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(CommunicateWay entity) {
        return LookupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(CommunicateWay entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
