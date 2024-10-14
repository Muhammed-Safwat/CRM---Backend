package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Broker;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.BrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;

@Service
public class BrokerService extends BaseLookupServiceImpl<Broker, LockupDTO> {

    public BrokerService(BrokerRepository repository) {
        super(repository);
    }

    @Override
    protected Broker mapDtoToEntity(LockupDTO dto, Transition transition) {
        return Broker.builder()
                .name(dto.getName())
                .id(dto.getId())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Broker entity, LockupDTO dto) {
        entity.setName(dto.getName());
    }
}
