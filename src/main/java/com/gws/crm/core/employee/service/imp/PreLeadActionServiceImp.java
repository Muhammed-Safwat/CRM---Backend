package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.employee.mapper.ActionMapper;
import com.gws.crm.core.employee.repository.UserActionRepository;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.leads.repository.GenericBaseLeadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PreLeadActionServiceImp extends GenericLeadActionServiceImp<PreLead> {


    protected PreLeadActionServiceImp(UserRepository userRepository, GenericBaseLeadRepository<PreLead> leadRepository, UserActionRepository userActionRepository, ActionMapper actionMapper) {
        super(userRepository, leadRepository, userActionRepository, actionMapper);
    }

    @Override
    public ResponseEntity<?> setActionOnLead(ActionOnLeadDTO action, Transition transition) {
        return null;
    }

    @Override
    public void setSalesLeadCreationAction(PreLead salesLead, Transition transition) {

    }

    @Override
    public void setSalesAssignAction(PreLead salesLead, Transition transition) {

    }

    @Override
    public void setSalesViewLeadAction(PreLead salesLead, Transition transition) {

    }
}
