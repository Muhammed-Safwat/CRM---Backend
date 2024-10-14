package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Area;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends BaseLookupRepository<Area> {

    @Query("SELECT a.name FROM Area a WHERE a.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}
