package com.gws.crm.core.resale.service;

import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

public interface ResaleLookupsService {
    ResponseEntity<?> getLookups(Transition transition);
}
