package com.gws.crm.core.employee.mapper;

import com.gws.crm.core.employee.dto.ActionResponse;
import com.gws.crm.core.employee.entity.ActionOnLead;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActionMapper {

    private final EmployeeMapper employeeMapper;


    public ActionResponse toDto(ActionOnLead actionOnLead) {
        return ActionResponse.builder()
                .id(actionOnLead.getId())
                .type(actionOnLead.getType().getDisplayValue())
                .creator(employeeMapper.toSimpleDto(actionOnLead.getCreator()))
                .build();
    }

    public List<ActionResponse> toDto(List<ActionOnLead> actionOnLeadList) {
        return actionOnLeadList.stream().map(this::toDto).toList();
    }

    public Page<ActionResponse> toDto(Page<ActionOnLead> actionOnLeadPage) {
        return actionOnLeadPage.map(this::toDto);
    }
}
