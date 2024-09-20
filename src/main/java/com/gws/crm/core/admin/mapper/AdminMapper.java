package com.gws.crm.core.admin.mapper;

import com.gws.crm.core.admin.dto.AdminBasicsInfo;
import com.gws.crm.core.admin.entity.Admin;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    AdminBasicsInfo toDto(Admin admin);

    List<AdminBasicsInfo> toDtoList(List<Admin> admin);
}
