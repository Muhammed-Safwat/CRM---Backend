package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.InvestmentGoal;
import com.gws.crm.core.lookups.repository.InvestmentGoalRepository;
import org.springframework.stereotype.Service;

@Service
public class InvestmentGoalService extends BaseLookupServiceImpl<InvestmentGoal, LookupDTO> {

    public InvestmentGoalService(InvestmentGoalRepository repository) {
        super(repository);
    }

    @Override
    protected InvestmentGoal mapDtoToEntity(LookupDTO dto, Transition transition) {
        return InvestmentGoal.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(InvestmentGoal entity) {
        return LookupDTO.builder()
                .name(entity.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(InvestmentGoal entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
