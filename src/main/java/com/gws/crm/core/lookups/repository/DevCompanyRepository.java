package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.DevCompany;
import com.gws.crm.core.lookups.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DevCompanyRepository extends BaseLookupRepository<DevCompany> {

    @Query("SELECT dc.name FROM DevCompany dc WHERE dc.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);

    Optional<DevCompany> getByName(String name);
}