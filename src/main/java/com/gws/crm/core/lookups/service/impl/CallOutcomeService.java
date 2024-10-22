package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.CallOutcome;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;


@Service
public class CallOutcomeService extends BaseLookupServiceImpl<CallOutcome, LookupDTO> {

    public CallOutcomeService(BaseLookupRepository<CallOutcome> repository) {
        super(repository);
    }

    @Override
    protected CallOutcome mapDtoToEntity(LookupDTO dto, Transition transition) {
        return CallOutcome.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(CallOutcome entity) {
        return LookupDTO.builder().id(entity.getId()).name(entity.getName()).build();
    }

    @Override
    protected void updateEntityFromDto(CallOutcome entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}

