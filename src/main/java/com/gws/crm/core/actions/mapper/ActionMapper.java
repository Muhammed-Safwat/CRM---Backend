package com.gws.crm.core.actions.mapper;

import com.gws.crm.core.actions.dtos.ActionResponse;
import com.gws.crm.core.actions.entity.LeadActionDetails;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.employee.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActionMapper {

    private final EmployeeMapper employeeMapper;

    public ActionResponse toDto(UserAction userAction) {
        LeadActionDetails leadDetails = userAction.getLeadDetails();

        return ActionResponse.builder()
                .id(userAction.getId())
                .type(userAction.getType().getDisplayValue())
                .description(userAction.getDescription())
                .createdAt(userAction.getCreatedAt())
                .creator(employeeMapper.toSimpleDto(userAction.getCreator()))
                .comment(leadDetails != null ? leadDetails.getComment() : null)
                .callBackTime(leadDetails != null ? leadDetails.getCallBackTime() : null)
                .cancellationReason(leadDetails != null ? leadDetails.getCancellationReason() : null)
                .stage(leadDetails != null ? leadDetails.getStage() : null)
                .nextActionDate(leadDetails != null ? leadDetails.getNextActionDate() : null)
                .callOutcome(leadDetails != null && leadDetails.getCallOutcome() != null
                        ? leadDetails.getCallOutcome().getName()
                        : null)
                .leadId(leadDetails != null ? leadDetails.getLead().getId() : null)
                .build();
    }

    public List<ActionResponse> toDto(List<UserAction> UserActionList) {
        return UserActionList.stream()
                .map(this::toDto)
                .toList();
    }

    public Page<ActionResponse> toDto(Page<UserAction> userActionPage) {
        return userActionPage.map(this::toDto);
    }

}
