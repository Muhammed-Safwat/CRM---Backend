package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.repository.ChannelRepository;
import org.springframework.stereotype.Service;

@Service
public class ChannelService extends BaseLookupServiceImpl<Channel, LookupDTO> {

    public ChannelService(ChannelRepository repository) {
        super(repository);
    }

    @Override
    protected Channel mapDtoToEntity(LookupDTO dto, Transition transition) {
        return Channel.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(Channel entity) {
        return LookupDTO.builder().id(entity.getId()).name(entity.getName()).build();
    }

    @Override
    protected void updateEntityFromDto(Channel entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
