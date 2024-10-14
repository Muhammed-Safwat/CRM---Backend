package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.ChannelRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class ChannelService extends BaseLookupServiceImpl<Channel, LockupDTO> {

    public ChannelService(ChannelRepository repository) {
        super(repository);
    }

    @Override
    protected Channel mapDtoToEntity(LockupDTO dto, Transition transition) {
        return Channel.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Channel entity, LockupDTO dto) {
        entity.setName(dto.getName());
    }
}
