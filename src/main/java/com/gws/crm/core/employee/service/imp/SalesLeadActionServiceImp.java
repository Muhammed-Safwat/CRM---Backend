package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.core.employee.mapper.ActionMapper;
import com.gws.crm.core.employee.repository.UserActionRepository;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Log
public class SalesLeadActionServiceImp extends GenericSalesLeadActionServiceImp<Lead> {

    protected SalesLeadActionServiceImp(UserRepository userRepository, LeadRepository leadRepository,
                                        UserActionRepository userActionRepository, ActionMapper actionMapper, CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        super(userRepository, leadRepository, userActionRepository, actionMapper, callOutcomeRepository, cancelReasonsRepository, stageRepository);
    }
}
