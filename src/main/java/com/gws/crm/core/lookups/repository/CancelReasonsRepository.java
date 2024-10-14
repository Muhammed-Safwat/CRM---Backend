package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.CancelReasons;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CancelReasonsRepository extends BaseLookupRepository<CancelReasons> {

    @Query("SELECT cr.name FROM CancelReasons cr WHERE cr.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}
