package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.dto.PropertyTypeDTO;
import com.gws.crm.core.lookups.entity.PropertyType;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import com.gws.crm.core.lookups.repository.PropertyTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
public class PropertyTypeService extends BaseLookupServiceImpl<PropertyType, PropertyTypeDTO> {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

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

    public ResponseEntity<?> getPropertyTypeByCategoryId(Long categoryId) {
        List<PropertyType> propertyTypeList = propertyTypeRepository.getByCategoryId(categoryId);
        return success(propertyTypeList);
    }

}
