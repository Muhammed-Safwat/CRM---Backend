package com.gws.crm.core.employee.mapper;

import com.gws.crm.core.employee.dto.CompanyDTO;
import com.gws.crm.core.employee.entity.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDTO toDto(Company company);

    Company toEntity(CompanyDTO companyDTO);

    List<CompanyDTO> toDtoList(List<Company> companies);

    List<Company> toEntityList(List<CompanyDTO> companyDTOs);
}
