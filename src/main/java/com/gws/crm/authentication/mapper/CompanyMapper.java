package com.gws.crm.authentication.mapper;

import com.gws.crm.authentication.dto.CompanyDTO;
import com.gws.crm.authentication.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);
    CompanyDTO toDto(Company company);
    Company toEntity(CompanyDTO companyDTO);
    List<CompanyDTO> toDtoList(List<Company> companies);
    List<Company> toEntityList(List<CompanyDTO> companyDTOs);
}
