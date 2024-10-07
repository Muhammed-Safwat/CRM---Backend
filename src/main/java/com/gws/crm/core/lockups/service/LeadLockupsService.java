package com.gws.crm.core.lockups.service;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.LeadLockupsDTO;
import org.springframework.http.ResponseEntity;

public interface LeadLockupsService {
    ResponseEntity<?> getLeadLockups(Transition transition);
}
