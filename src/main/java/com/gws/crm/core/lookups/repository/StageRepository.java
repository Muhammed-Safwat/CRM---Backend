package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Stage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageRepository extends BaseLookupRepository<Stage> {

    @Query("SELECT c.name FROM Stage c WHERE c.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}