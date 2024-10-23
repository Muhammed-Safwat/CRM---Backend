package com.gws.crm.core.employee.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.leads.entity.SalesLead;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface ActionService<T extends SalesLead> {
    ResponseEntity<?> setActionOnLead(@Valid ActionOnLeadDTO action, Transition transition);
}
