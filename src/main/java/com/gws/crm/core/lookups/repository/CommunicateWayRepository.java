package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.CommunicateWay;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunicateWayRepository extends BaseLookupRepository<CommunicateWay> {

    @Query("SELECT cw.name FROM CommunicateWay cw WHERE cw.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}
