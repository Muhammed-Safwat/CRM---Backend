package com.gws.crm.core.actions.dtos;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ActionResponse {

    private long id;

    private EmployeeSimpleDTO creator;

    private String type;

    private String description;

    private String comment;

    private LocalDateTime createdAt;

    private String callOutcome;

    private LocalDateTime nextActionDate;

    private String cancellationReason;

    private String stage;

    private LocalDateTime callBackTime;

    private long leadId;
}
