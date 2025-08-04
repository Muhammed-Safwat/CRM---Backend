package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.entity.Employee;
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
        long id = transition.getUserId();
        if ("USER".equals(transition.getRole())) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(NotFoundResourceException::new);
            id = employee.getAdmin().getId();
        }
        List<Broker> brokers = brokerRepository.findAllByAdminId(id);
        List<LeadStatus> leadStatuses = leadStatusRepository.findAll();
        List<InvestmentGoal> investmentGoals = investmentGoalRepository.findAllByAdminId(id);
        List<Project> projects = projectRepository.findAllByAdminId(id);
        List<CancelReasons> cancelReasons = cancelReasonsRepository.findAllByAdminId(id);
        List<EmployeeSimpleDTO> salesReps = employeeRepository.findAllByAdminId(id)
                .stream()
                .map(employee -> EmployeeSimpleDTO.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .jobName(employee.getJobName())
                        .build()
                )
                .collect(Collectors.toList());
        List<Channel> channels = channelRepository.findAllByAdminId(id);
        List<CommunicateWay> communicateWays = communicateWayRepository.findAllByAdminId(id);
        List<CallOutcome> callOutcomes = callOutcomeRepository.findAllByAdminId(id);
        List<Stage> stages = stageRepository.findAllByAdminId(id);

        LeadLookupsDTO leadLookupsDTO = LeadLookupsDTO.builder()
                .brokers(brokers)
                .leadStatuses(leadStatuses)
                .investmentGoals(investmentGoals)
                .projects(projects)
                .cancelReasons(cancelReasons)
                .salesReps(salesReps)
                .channels(channels)
                .communicateWays(communicateWays)
                .actions(callOutcomes)
                .stages(stages)
                .build();

        return success(leadLookupsDTO);
    }

    @Override
    public ResponseEntity<?> getActionLookups(Transition transition) {
        long id = transition.getUserId();
        if ("USER".equals(transition.getRole())) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(NotFoundResourceException::new);
            id = employee.getAdmin().getId();
        }
        List<CancelReasons> cancelReasons = cancelReasonsRepository.findAllByAdminId(id);
        List<CallOutcome> callOutcomes = callOutcomeRepository.findAllByAdminId(id);
        List<Stage> stages = stageRepository.findAllByAdminId(id);

        ActionLookupDTO actionLookupDTO = ActionLookupDTO.builder()
                .cancelReasons(cancelReasons)
                .callOutcome(callOutcomes)
                .stages(stages)
                .build();

        return success(actionLookupDTO);
    }

}

