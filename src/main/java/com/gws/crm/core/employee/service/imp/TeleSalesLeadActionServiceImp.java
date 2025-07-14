package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.core.employee.mapper.ActionMapper;
import com.gws.crm.core.employee.repository.UserActionRepository;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.leads.repository.TeleSalesLeadRepository;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import org.springframework.stereotype.Service;

@Service
public class TeleSalesLeadActionServiceImp extends GenericSalesLeadActionServiceImp<TeleSalesLead> {

    protected TeleSalesLeadActionServiceImp(UserRepository userRepository, TeleSalesLeadRepository leadRepository,
                                            UserActionRepository userActionRepository, ActionMapper actionMapper, CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        super(userRepository, leadRepository, userActionRepository, actionMapper, callOutcomeRepository, cancelReasonsRepository, stageRepository);
    }
}
