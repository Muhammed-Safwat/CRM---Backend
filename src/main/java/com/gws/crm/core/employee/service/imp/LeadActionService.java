package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.repository.SalesLeadRepository;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import org.springframework.stereotype.Service;

@Service
public class LeadActionService extends ActionServiceImp<Lead> {

    public LeadActionService(UserRepository userRepository, SalesLeadRepository<Lead> leadRepository,
                             CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        super(userRepository, leadRepository, callOutcomeRepository, cancelReasonsRepository, stageRepository);
    }
}
