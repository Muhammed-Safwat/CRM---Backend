package com.gws.crm.core.admin.mapper;

import com.gws.crm.core.admin.dto.AdminBasicsInfo;
import com.gws.crm.core.admin.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Mapping(source = "admin.company",target = "companyDTO")
    AdminBasicsInfo toDto(Admin admin);

    List<AdminBasicsInfo> toDtoList(List<Admin> admin);
}
