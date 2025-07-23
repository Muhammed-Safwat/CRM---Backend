package com.gws.crm.core.actions.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionOnLeadDTO {

    private Long leadId;
    private String actionType;
    private Long callOutcome;
    private LocalDateTime nextActionDate;
    private Long stage;
    private Long cancellationReason;
    private LocalDateTime callBackTime;
    private String comment;

}
