package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseLookupServiceImpl<Category, LookupDTO> {

    public CategoryService(CategoryRepository repository) {
        super(repository);
    }

    @Override
    protected Category mapDtoToEntity(LookupDTO dto, Transition transition) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    protected LookupDTO mapEntityToDto(Category entity) {
        return LookupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    @Override
    protected void updateEntityFromDto(Category entity, LookupDTO dto) {
        entity.setName(dto.getName());
    }
}
