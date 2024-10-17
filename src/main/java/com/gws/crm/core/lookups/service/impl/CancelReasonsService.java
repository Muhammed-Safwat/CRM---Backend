package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import org.springframework.stereotype.Service;

@Service
public class CancelReasonsService extends BaseLookupServiceImpl<CancelReasons, LookupDTO> {

    public CancelReasonsService(CancelReasonsRepository repository) {
        super(repository);
    }

    @Override
    protected CancelReasons mapDtoToEntity(LookupDTO dto, Transition transition) {
        return CancelReasons.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(CancelReasons entity) {
        return LookupDTO.builder()
                .name(entity.getName())
                .id(entity.getId())
                .build();
    }

    @Override
    protected void updateEntityFromDto(CancelReasons entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
