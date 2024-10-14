package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.entity.InvestmentGoal;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.InvestmentGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestmentGoalService extends BaseLookupServiceImpl<InvestmentGoal, LockupDTO> {

    public InvestmentGoalService(InvestmentGoalRepository repository) {
        super(repository);
    }

    @Override
    protected InvestmentGoal mapDtoToEntity(LockupDTO dto, Transition transition) {
        return InvestmentGoal.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(InvestmentGoal entity, LockupDTO dto) {
        entity.setName(dto.getName());
    }
}
