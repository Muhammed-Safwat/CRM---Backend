package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.RegionDto;
import org.springframework.http.ResponseEntity;

public interface RegionService {
    ResponseEntity<?> getRegions(int page, int size, Transition transition);

    ResponseEntity<?> getRegionById(long id, Transition transition);

    ResponseEntity<?> createRegion(RegionDto regionDto, Transition transition);

    ResponseEntity<?> updateRegion(RegionDto regionDto, Transition transition);

    ResponseEntity<?> deleteRegion(long id, Transition transition);

    ResponseEntity<?> getAllRegions(Transition transition);
}
