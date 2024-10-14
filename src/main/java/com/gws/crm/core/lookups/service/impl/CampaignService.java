package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Campaign;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;

@Service
public class CampaignService extends BaseLookupServiceImpl<Campaign, LookupDTO> {
    public CampaignService(BaseLookupRepository<Campaign> repository) {
        super(repository);
    }

    @Override
    protected Campaign mapDtoToEntity(LookupDTO dto, Transition transition) {
        return Campaign.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(Campaign entity) {
        return LookupDTO.builder().id(entity.getId()).name(entity.getName()).build();
    }

    @Override
    protected void updateEntityFromDto(Campaign entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
