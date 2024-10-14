package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class CancelReasonsService  extends BaseLookupServiceImpl<CancelReasons, LockupDTO> {

    public CancelReasonsService(BaseLookupRepository<CancelReasons> repository) {
        super(repository);
    }

    @Override
    protected CancelReasons mapDtoToEntity(LockupDTO dto, Transition transition) {
        return CancelReasons.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(CancelReasons entity, LockupDTO dto) {
        entity.setName(dto.getName());
    }
}
