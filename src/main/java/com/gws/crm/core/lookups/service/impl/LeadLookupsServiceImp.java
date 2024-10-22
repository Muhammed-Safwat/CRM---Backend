package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.lookups.dto.ActionLookupDTO;
import com.gws.crm.core.lookups.dto.LeadLookupsDTO;
import com.gws.crm.core.lookups.entity.*;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.lookups.service.LeadLookupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class LeadLookupsServiceImp implements LeadLookupsService {

    private final BrokerRepository brokerRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final InvestmentGoalRepository investmentGoalRepository;
    private final ProjectRepository projectRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final EmployeeRepository employeeRepository;
    private final ChannelRepository channelRepository;
    private final CommunicateWayRepository communicateWayRepository;
    private final CallOutcomeRepository callOutcomeRepository;
    private final StageRepository stageRepository;

    @Override
    public ResponseEntity<?> getLeadLookups(Transition transition) {
        List<Broker> brokers = brokerRepository.findAllByAdminId(transition.getUserId());
        List<LeadStatus> leadStatuses = leadStatusRepository.findAll();
        List<InvestmentGoal> investmentGoals = investmentGoalRepository.findAllByAdminId(transition.getUserId());
        List<Project> projects = projectRepository.findAllByAdminId(transition.getUserId());
        List<CancelReasons> cancelReasons = cancelReasonsRepository.findAllByAdminId(transition.getUserId());
        List<EmployeeSimpleDTO> salesReps = employeeRepository.findAllByAdminId(transition.getUserId())
                .stream()
                .map(employee -> EmployeeSimpleDTO.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .jobName(employee.getJobName().getJobName())
                        .build()
                )
                .collect(Collectors.toList());
        List<Channel> channels = channelRepository.findAllByAdminId(transition.getUserId());
        List<CommunicateWay> communicateWays = communicateWayRepository.findAllByAdminId(transition.getUserId());

        LeadLookupsDTO leadLookupsDTO = LeadLookupsDTO.builder()
                .brokers(brokers)
                .leadStatuses(leadStatuses)
                .investmentGoals(investmentGoals)
                .projects(projects)
                .cancelReasons(cancelReasons)
                .salesReps(salesReps)
                .channels(channels)
                .communicateWays(communicateWays)
                .build();

        return success(leadLookupsDTO);
    }

    @Override
    public ResponseEntity<?> getActionLookups(Transition transition) {
        List<CancelReasons> cancelReasons = cancelReasonsRepository.findAllByAdminId(transition.getUserId());
        List<CallOutcome> callOutcomes = callOutcomeRepository.findAllByAdminId(transition.getUserId());
        List<Stage> stages = stageRepository.findAllByAdminId(transition.getUserId());

        ActionLookupDTO actionLookupDTO = ActionLookupDTO.builder()
                .cancelReasons(cancelReasons)
                .callOutcome(callOutcomes)
                .stages(stages)
                .build();

        return success(actionLookupDTO);
    }


}

