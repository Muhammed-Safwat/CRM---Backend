package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.CommunicateWay;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class CommunicateWayService  extends BaseLookupServiceImpl<CommunicateWay,LockupDTO> {

    public CommunicateWayService(BaseLookupRepository<CommunicateWay> repository) {
        super(repository);
    }

    @Override
    protected CommunicateWay mapDtoToEntity(LockupDTO dto, Transition transition) {
        return null;
    }

    @Override
    protected void updateEntityFromDto(CommunicateWay entity, LockupDTO dto) {

    }
}
