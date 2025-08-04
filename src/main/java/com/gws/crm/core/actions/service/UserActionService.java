package com.gws.crm.core.actions.service;

import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

public interface UserActionService {
    ResponseEntity<?> getActions(long leadId, int page, int size, Transition transition);
}
