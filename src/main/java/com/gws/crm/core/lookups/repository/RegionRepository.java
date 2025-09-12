package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Region;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends BaseLookupRepository<Region> {

    @Query("SELECT r.name FROM Region r WHERE r.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);

    Optional<Region> getByName(String name);
}
