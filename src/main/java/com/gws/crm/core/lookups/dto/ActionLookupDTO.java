package com.gws.crm.core.lookups.dto;

import com.gws.crm.core.lookups.entity.CallOutcome;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.entity.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActionLookupDTO {
    private List<CallOutcome> callOutcome;
    private List<CancelReasons> cancelReasons;
    private List<Stage> stages;
}
