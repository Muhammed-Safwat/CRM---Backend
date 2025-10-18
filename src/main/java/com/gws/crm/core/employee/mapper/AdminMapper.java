package com.gws.crm.core.employee.mapper;

import com.gws.crm.core.employee.dto.AdminBasicsInfo;
import com.gws.crm.core.employee.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(source = "admin.company", target = "companyDTO")
    AdminBasicsInfo toDto(Admin admin);

    List<AdminBasicsInfo> toDtoList(List<Admin> admin);
}
