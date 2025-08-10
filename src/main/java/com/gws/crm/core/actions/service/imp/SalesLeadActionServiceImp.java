package com.gws.crm.core.actions.service.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.core.actions.mapper.ActionMapper;
import com.gws.crm.core.actions.repository.UserActionRepository;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Log
public class SalesLeadActionServiceImp extends GenericSalesLeadActionServiceImp<Lead> {

    protected SalesLeadActionServiceImp(UserRepository userRepository, LeadRepository leadRepository,
                                        UserActionRepository userActionRepository, ActionMapper actionMapper, CallOutcomeRepository callOutcomeRepository,
                                        CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        super(userRepository, leadRepository, userActionRepository, actionMapper, callOutcomeRepository, cancelReasonsRepository, stageRepository);
    }


}
