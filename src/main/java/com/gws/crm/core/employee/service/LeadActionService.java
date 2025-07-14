package com.gws.crm.core.employee.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.leads.entity.BaseLead;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface LeadActionService<T extends BaseLead> {

    ResponseEntity<?> setActionOnLead(@Valid ActionOnLeadDTO action, Transition transition);

    void setSalesLeadCreationAction(T salesLead, Transition transition);

    void setLeadEditAction(T salesLead, Transition transition);

    void setLeadRestoreAction(T salesLead, Transition transition);

    void setSalesAssignAction(T salesLead, Transition transition);

    void setLeadDeletionAction(T salesLead, Transition transition);

    ResponseEntity<?> getActions(long leadId, Transition transition);

    void setSalesViewLeadAction(T salesLead, Transition transition);

}
