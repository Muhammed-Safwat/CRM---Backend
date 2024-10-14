package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.LeadStatus;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import org.springframework.stereotype.Service;

@Service
public class LeadStatusService extends BaseLookupServiceImpl<LeadStatus, LookupDTO> {


    public LeadStatusService(BaseLookupRepository<LeadStatus> repository) {
        super(repository);
    }

    @Override
    protected LeadStatus mapDtoToEntity(LookupDTO dto, Transition transition) {
        return LeadStatus
                .builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(LeadStatus entity) {
        return LookupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(LeadStatus entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
