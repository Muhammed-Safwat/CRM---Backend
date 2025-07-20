package com.gws.crm.core.employee.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.PreLead;
import jakarta.validation.Valid;
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
