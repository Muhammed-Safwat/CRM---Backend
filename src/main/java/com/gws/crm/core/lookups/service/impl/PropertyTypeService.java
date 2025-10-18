package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.entity.Admin;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.dto.PropertyTypeDTO;
import com.gws.crm.core.lookups.entity.Category;
import com.gws.crm.core.lookups.entity.PropertyType;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.repository.CategoryRepository;
import com.gws.crm.core.lookups.repository.LookupRepositoryUtils;
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

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AdminRepository adminRepository;

    public PropertyTypeService(BaseLookupRepository<PropertyType> repository) {
        super(repository);
    }

    @Override
    protected PropertyType mapDtoToEntity(PropertyTypeDTO dto, Transition transition) {
       Admin admin = adminRepository.findById(transition.getUserId())
               .orElseThrow(NotFoundResourceException::new);
        return PropertyType.builder()
                .name(dto.getName())
                .category(resolveCategory(dto.getCategory(),admin))
                .build();
    }

    @Override
    protected PropertyTypeDTO mapEntityToDto(PropertyType entity) {

        return PropertyTypeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory().getName())
                .build();
    }
    private Category resolveCategory(String name, Admin admin) {
        return LookupRepositoryUtils.getOrCreateByAttribute(
                categoryRepository,
                name,
                categoryRepository::getByName,
                () -> {
                    Category c = new Category();
                    c.setName(name);
                    c.setAdmin(admin);
                    return c;
                }
        );
    }
    @Override
    protected void updateEntityFromDto(PropertyType entity, PropertyTypeDTO dto) {
        entity.setName(dto.getName());
        entity.setCategory(repository.findByName(dto.getCategory())
                .orElseThrow(NotFoundResourceException::new));
    }

    public ResponseEntity<?> getPropertyTypeByCategoryId(Long categoryId) {
        List<PropertyType> propertyTypeList = propertyTypeRepository.getByCategoryId(categoryId);
        return success(propertyTypeList);
    }

}
