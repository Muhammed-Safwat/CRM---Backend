package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.repository.ChannelRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class StageService extends BaseLookupServiceImpl<Stage,LockupDTO>  {

    public StageService(StageRepository repository) {
        super(repository);
    }

    @Override
    protected Stage mapDtoToEntity(LockupDTO dto, Transition transition) {
        return Stage.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Stage entity, LockupDTO dto) {
        entity.setName(dto.getName());
    }
}
