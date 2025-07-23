package com.gws.crm.common.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.actions.repository.repository.EmployeeRepository;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.lookups.repository.*;
import com.gws.crm.core.resale.repository.ResaleStatusRepository;
import com.gws.crm.core.resale.repository.ResaleTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
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
        long id = transition.getUserId();
        if (transition.getRole().equals("USER")) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(NotFoundResourceException::new);
            id = employee.getAdmin().getId();
        }
        log.info("Id ============================> {}", id);
        List<String> brokers = brokerRepository.findAllNamesByAdminId(transition.getUserId());
        List<String> leadStatuses = leadStatusRepository.findAllNames();
        List<String> investmentGoals = investmentGoalRepository.findAllNamesByAdminId(id);
        List<String> projects = projectRepository.findAllNamesByAdminId(id);
        List<String> cancelReasons = cancelReasonsRepository.findAllNamesByAdminId(id);
        List<String> channels = channelRepository.findAllNamesByAdminId(id);
        List<String> communicateWays = communicateWayRepository.findAllNamesByAdminId(id);

        Map<String, List<String>> lookupsMap = new HashMap<>();
        if (transition.getRole().equals("ADMIN")) {
            List<String> salesReps = employeeRepository.findAllNamesByAdminId(id);
            lookupsMap.put("salesRep", salesReps);
        }
        lookupsMap.put("broker", brokers);
        lookupsMap.put("status", leadStatuses);
        lookupsMap.put("investmentGoal", investmentGoals);
        lookupsMap.put("project", projects);
        lookupsMap.put("cancelReason", cancelReasons);
        lookupsMap.put("channel", channels);
        lookupsMap.put("communicateWay", communicateWays);
        return lookupsMap;
    }

    @Override
    public Map<String, List<String>> generatePreLeadExcelSheetMap(Transition transition) {
        long id = transition.getUserId();
        if (transition.getRole().equals("USER")) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(NotFoundResourceException::new);
            id = employee.getAdmin().getId();
        }
        List<String> projects = projectRepository.findAllNamesByAdminId(id);
        List<String> channels = channelRepository.findAllNamesByAdminId(id);
        Map<String, List<String>> lookupsMap = new HashMap<>();

        lookupsMap.put("project", projects);
        lookupsMap.put("channel", channels);
        return lookupsMap;
    }

    @Override
    public Map<String, List<String>> generateResaleSheetMap(Transition transition) {
        long id = transition.getUserId();
        if (transition.getRole().equals("USER")) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(NotFoundResourceException::new);
            id = employee.getAdmin().getId();
        }
        List<String> projects = projectRepository.findAllNamesByAdminId(id);
        List<String> categories = categoryRepository.findAllNamesByAdminId(id);
        List<String> types = resaleTypeRepository.findAllNames();
        List<String> statuses = resaleStatusRepository.findAllNames();

        Map<String, List<String>> lookupsMap = new HashMap<>();
        if (transition.getRole().equals("ADMIN")) {
            List<String> salesReps = employeeRepository.findAllNamesByAdminId(id);
            lookupsMap.put("salesRep", salesReps);
        }
        lookupsMap.put("type", types);
        lookupsMap.put("status", statuses);
        lookupsMap.put("project", projects);
        lookupsMap.put("category", categories);

        return lookupsMap;
    }
}
