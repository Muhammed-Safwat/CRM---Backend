package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.lockups.dto.RegionDto;
import com.gws.crm.core.lockups.entity.Region;
import com.gws.crm.core.lockups.repository.RegionRepository;
import com.gws.crm.core.lockups.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public ResponseEntity<?> getRegions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Region> regionPage = regionRepository.findAll(pageable);
        return success(regionPage);
    }

    @Override
    public ResponseEntity<?> getRegionById(long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        return success(region);
    }

    @Override
    public ResponseEntity<?> createRegion(RegionDto regionDto) {
        Region region = Region.builder().name(regionDto.getName()).build();
        regionRepository.save(region);
        return success(region);
    }

    @Override
    public ResponseEntity<?> updateRegion(RegionDto regionDto) {
        Region region = regionRepository.findById(regionDto.getId())
                .orElseThrow(NotFoundResourceException::new);
        region.setName(regionDto.getName());
        regionRepository.save(region);
        return success(region);
    }

    @Override
    public ResponseEntity<?> deleteRegion(long id) {
        regionRepository.deleteById(id);
        return success("Region deleted successfully");
    }

    @Override
    public ResponseEntity<?> getAllRegions() {
        return success(regionRepository.findAll());
    }

}
