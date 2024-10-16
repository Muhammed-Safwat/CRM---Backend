package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.dto.PropertyTypeDTO;
import com.gws.crm.core.lookups.entity.PropertyType;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyTypeService extends BaseLookupServiceImpl<PropertyType, PropertyTypeDTO> {

    @Autowired
    private CategoryRepository repository;

    public PropertyTypeService(BaseLookupRepository<PropertyType> repository) {
        super(repository);
    }

    @Override
    protected PropertyType mapDtoToEntity(PropertyTypeDTO dto, Transition transition) {
        return PropertyType.builder()
                .name(dto.getName())
                .category(repository.getReferenceById(dto.getCategory().getId()))
                .build();
    }

    @Override
    protected PropertyTypeDTO mapEntityToDto(PropertyType entity) {
        return PropertyTypeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(LookupDTO.builder()
                        .id(entity.getCategory().getId())
                        .name(entity.getCategory().getName())
                        .build())
                .build();
    }

    @Override
    protected void updateEntityFromDto(PropertyType entity, PropertyTypeDTO dto) {
        entity.setName(dto.getName());
        entity.setCategory(repository.getReferenceById(dto.getCategory().getId()));
    }
}
