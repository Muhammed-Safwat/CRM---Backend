package com.gws.crm.common.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.resale.repository.ResaleStatusRepository;
import com.gws.crm.core.resale.repository.ResaleTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelSheetServiceImp implements ExcelSheetService {

    private final BrokerRepository brokerRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final InvestmentGoalRepository investmentGoalRepository;
    private final ProjectRepository projectRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final EmployeeRepository employeeRepository;
    private final ChannelRepository channelRepository;
    private final CommunicateWayRepository communicateWayRepository;
    private final CategoryRepository categoryRepository;
    private final ResaleTypeRepository resaleTypeRepository;
    private final ResaleStatusRepository resaleStatusRepository;


    @Override
    public Map<String, List<String>> generateLeadExcelSheetMap(Transition transition) {
        List<String> brokers = brokerRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> leadStatuses = leadStatusRepository.findAllNames();
        List<String> investmentGoals = investmentGoalRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> projects = projectRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> cancelReasons = cancelReasonsRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> channels = channelRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> communicateWays = communicateWayRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> salesReps = employeeRepository.findAllNamesByAdminId(transition.getUserId());

        Map<String, List<String>> lookupsMap = new HashMap<>();

        lookupsMap.put("broker", brokers);
        lookupsMap.put("status", leadStatuses);
        lookupsMap.put("investmentGoal", investmentGoals);
        lookupsMap.put("project", projects);
        lookupsMap.put("cancelReason", cancelReasons);
        lookupsMap.put("channel", channels);
        lookupsMap.put("communicateWay", communicateWays);
        lookupsMap.put("salesRep", salesReps);

        return lookupsMap;
    }

    @Override
    public Map<String, List<String>> generatePreLeadExcelSheetMap(Transition transition) {
        List<String> projects = projectRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> categories = categoryRepository.findAllNamesByAdminId(transition.getUserId());
        Map<String, List<String>> lookupsMap = new HashMap<>();

        lookupsMap.put("project", projects);
        lookupsMap.put("category", categories);
        return lookupsMap;
    }

    @Override
    public Map<String, List<String>> generateResaleSheetMap(Transition transition) {

        List<String> projects = projectRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> categories = categoryRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> types = resaleTypeRepository.findAllNames();
        List<String> statuses = resaleStatusRepository.findAllNames();
        List<String> salesReps = employeeRepository.findAllNamesByAdminId(transition.getUserId());

        Map<String, List<String>> lookupsMap = new HashMap<>();

        lookupsMap.put("type", types);
        lookupsMap.put("status", statuses);
        lookupsMap.put("salesRep", salesReps);
        lookupsMap.put("project", projects);
        lookupsMap.put("category", categories);

        return lookupsMap;
    }
}
