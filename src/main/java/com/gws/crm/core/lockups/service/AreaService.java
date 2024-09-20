package com.gws.crm.core.lockups.service;

import com.gws.crm.core.lockups.dto.AreaDTO;
import org.springframework.http.ResponseEntity;

public interface AreaService {
    ResponseEntity<?> getAreas(int page, int size);

    ResponseEntity<?> getAreaById(long id);

    ResponseEntity<?> createArea(AreaDTO area);

    ResponseEntity<?> updateArea(AreaDTO area);

    ResponseEntity<?> deleteArea(long id);

    ResponseEntity<?> getAllAreas();
}
