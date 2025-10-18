package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends BaseLookupRepository<Area> {

    @Query("select a from Area a left join fetch a.region left join fetch a.admin where a.admin.id = :adminId")
    List<Area> findAllByAdminIdAndDeletedFalse(Long adminId);

    @Query("SELECT a.name FROM Area a WHERE a.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);

    @Query("select a from Area a left join fetch a.region left join fetch a.admin where a.admin.id = :adminId")
    Page<Area> findAllByAdminId(Pageable pageable, @Param("adminId") Long adminId);



}