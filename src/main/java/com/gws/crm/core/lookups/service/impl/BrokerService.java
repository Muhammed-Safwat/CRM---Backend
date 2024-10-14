package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Broker;
import com.gws.crm.core.lookups.repository.BrokerRepository;
import org.springframework.stereotype.Service;

@Service
public class BrokerService extends BaseLookupServiceImpl<Broker, LookupDTO> {

    public BrokerService(BrokerRepository repository) {
        super(repository);
    }

    @Override
    protected Broker mapDtoToEntity(LookupDTO dto, Transition transition) {
        return Broker.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(Broker entity) {
        return LookupDTO.builder().id(entity.getId()).name(entity.getName()).build();
    }

    @Override
    protected void updateEntityFromDto(Broker entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
