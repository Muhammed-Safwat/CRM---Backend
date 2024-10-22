package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Broker;
import com.gws.crm.core.lookups.entity.CallOutcome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CallOutcomeRepository extends BaseLookupRepository<CallOutcome> {

    @Query("SELECT b.name FROM Broker b WHERE b.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}