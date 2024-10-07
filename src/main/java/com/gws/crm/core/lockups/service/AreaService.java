package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.AreaDTO;
import org.springframework.http.ResponseEntity;

public interface AreaService {
    ResponseEntity<?> getAreas(int page, int size, Transition transition);

    ResponseEntity<?> getAreaById(long id, Transition transition);

    ResponseEntity<?> createArea(AreaDTO area, Transition transition);

    ResponseEntity<?> updateArea(AreaDTO area, Transition transition);

    ResponseEntity<?> deleteArea(long id, Transition transition);

    ResponseEntity<?> getAllAreas(Transition transition);
}
