package com.gws.crm.core.actions.service.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.core.actions.mapper.ActionMapper;
import com.gws.crm.core.actions.repository.UserActionRepository;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.leads.repository.TeleSalesLeadRepository;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import org.springframework.stereotype.Service;

@Service
public class TeleSalesLeadActionServiceImp extends GenericSalesLeadActionServiceImp<TeleSalesLead> {

    protected TeleSalesLeadActionServiceImp(UserRepository userRepository, TeleSalesLeadRepository leadRepository,
                                            UserActionRepository userActionRepository, ActionMapper actionMapper,
                                            CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository,
                                            StageRepository stageRepository) {
        super(userRepository, leadRepository, userActionRepository, actionMapper, callOutcomeRepository, cancelReasonsRepository, stageRepository);
    }


}
