package com.gws.crm.core.lockups.service;

import com.gws.crm.core.lockups.dto.RegionDto;
import org.springframework.http.ResponseEntity;

public interface RegionService {
    ResponseEntity<?> getRegions(int page, int size);

    ResponseEntity<?> getRegionById(long id);

    ResponseEntity<?> createRegion(RegionDto regionDto);

    ResponseEntity<?> updateRegion(RegionDto regionDto);

    ResponseEntity<?> deleteRegion(long id);

    ResponseEntity<?> getAllRegions();
}
