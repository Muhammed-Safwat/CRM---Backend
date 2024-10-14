package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.repository.StageRepository;
import org.springframework.stereotype.Service;

@Service
public class StageService extends BaseLookupServiceImpl<Stage,LookupDTO>  {

    public StageService(StageRepository repository) {
        super(repository);
    }

    @Override
    protected Stage mapDtoToEntity(LookupDTO dto, Transition transition) {
        return Stage
                .builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(Stage entity) {
        return LookupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Stage entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
