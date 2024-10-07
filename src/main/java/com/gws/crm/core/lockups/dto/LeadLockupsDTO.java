package com.gws.crm.core.lockups.dto;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.lockups.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LeadLockupsDTO {
    private List<Broker> brokers;
    private List<LeadStatus> leadStatuses;
    private List<InvestmentGoal> investmentGoals;
    private List<Project> projects;
    private List<CancelReasons> cancelReasons;
    private List<EmployeeSimpleDTO> salesReps;
    private List<Channel> channels;
    private List<CommunicateWay> communicateWays;
}
