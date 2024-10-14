package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.DevCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DevCompanyRepository extends BaseLookupRepository<DevCompany> {

        @Query("SELECT dc.name FROM DevCompany dc WHERE dc.admin.id = :adminId")
        List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
    }