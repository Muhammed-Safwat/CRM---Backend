package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.leads.repository.TeleSalesLeadRepository;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import org.springframework.stereotype.Service;

@Service
public class TeleSalesLeadActionService extends ActionServiceImp<TeleSalesLead> {

    public TeleSalesLeadActionService(UserRepository userRepository, TeleSalesLeadRepository leadRepository,
                                      CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        super(userRepository, leadRepository, callOutcomeRepository, cancelReasonsRepository, stageRepository);
    }
}
