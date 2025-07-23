package com.gws.crm.core.actions.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.BaseLead;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeadActionService<T extends BaseLead> {

    void setLeadCreationAction(T salesLead, Transition transition);

    void setLeadCreationAction(List<T> leads, Transition transition);

    void setLeadEditionAction(T salesLead, Transition transition);

    void setLeadRestoreAction(T salesLead, Transition transition);

    void setAssignAction(T salesLead, Transition transition);

    void setDeletionAction(T salesLead, Transition transition);

    ResponseEntity<?> getActions(long leadId, Transition transition);

    void setViewLeadAction(T salesLead, Transition transition);


}
