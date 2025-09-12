package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.entity.Admin;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.AreaDTO;
import com.gws.crm.core.lookups.entity.Area;
import com.gws.crm.core.lookups.entity.Region;
import com.gws.crm.core.lookups.repository.AreaRepository;
import com.gws.crm.core.lookups.repository.LookupRepositoryUtils;
import com.gws.crm.core.lookups.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.core.lookups.spcification.AreaSpecification.filter;

@Service
public class AreaService extends BaseLookupServiceImpl<Area, AreaDTO> {

    @Autowired
    private  RegionRepository regionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AreaRepository areaRepository;

    public AreaService(AreaRepository repository) {
        super(repository);
    }

    @Override
    public ResponseEntity<?> getAll(int page, int size,String keyword,  Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Area> areaPage = areaRepository.findAll(filter(keyword,transition),pageable);
        Page<AreaDTO> dtoPage = areaPage.map(this::mapEntityToDto);
        return ResponseEntity.ok(dtoPage);
    }

    private Region resolveRegion(String name, Admin admin) {
        return LookupRepositoryUtils.getOrCreateByAttribute(
                regionRepository,
                name,
                regionRepository::getByName,
                () -> {
                    Region r = new Region();
                    r.setName(name);
                    r.setAdmin(admin);
                    return r;
                }
        );
    }

    @Override
    protected AreaDTO mapEntityToDto(Area entity) {
        return AreaDTO.builder()
                .name(entity.getName())
                .id(entity.getId())
                .region(entity.getRegion()!= null? entity.getRegion().getName() : "")
                .build();
    }

    @Override
    protected void updateEntityFromDto(Area entity, AreaDTO dto) {
        entity.setRegion(regionRepository.findByName(dto.getRegion())
                .orElseThrow(NotFoundResourceException::new));
        entity.setName(dto.getName());
    }

    @Override
    protected Area mapDtoToEntity(AreaDTO dto, Transition transition) {
        Admin admin  = adminRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        return Area.builder()
                .name(dto.getName())
                .region(resolveRegion(dto.getRegion(),admin))
                .build();
    }
}