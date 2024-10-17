package com.gws.crm.core.lookups.service;


import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LeadLookupsService {
    ResponseEntity<?> getLeadLookups(Transition transition);

    Map<String, List<String>> generateLeadExcelSheetMap(Transition transition);

    Map<String, List<String>> generatePreLeadExcelSheetMap(Transition transition);
}
