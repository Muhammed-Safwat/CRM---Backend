package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.lockups.dto.AreaDTO;
import com.gws.crm.core.lockups.entity.Area;
import com.gws.crm.core.lockups.entity.Region;
import com.gws.crm.core.lockups.repository.AreaRepository;
import com.gws.crm.core.lockups.repository.RegionRepository;
import com.gws.crm.core.lockups.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;
    private final RegionRepository regionRepository;

    @Override
    public ResponseEntity<?> getAreas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Area> areaPage = areaRepository.findAll(pageable);
        return success(areaPage);
    }

    @Override
    public ResponseEntity<?> getAreaById(long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        return success(area);
    }

    @Override
    public ResponseEntity<?> createArea(AreaDTO areaDTO) {
        Region region = regionRepository.findById(areaDTO.getRegion().getId())
                .orElseThrow(NotFoundResourceException::new);
        Area area = Area.builder().name(areaDTO.getName()).region(region).build();
        Area savedArea = areaRepository.save(area);
        return success(savedArea);
    }

    @Override
    public ResponseEntity<?> updateArea(AreaDTO areaDTO) {
        Area area = areaRepository.findById(areaDTO.getId())
                .orElseThrow(RuntimeException::new);
        Region region = regionRepository.findById(areaDTO.getRegion().getId())
                .orElseThrow(NotFoundResourceException::new);
        area.setName(areaDTO.getName());
        area.setRegion(region);
        areaRepository.save(area);
        return success(area);
    }

    @Override
    public ResponseEntity<?> deleteArea(long id) {
        areaRepository.deleteById(id);
        return success("Area deleted successfully");
    }

    @Override
    public ResponseEntity<?> getAllAreas() {
        List<Area> areas = areaRepository.findAll();
        return success(areas);
    }
}
