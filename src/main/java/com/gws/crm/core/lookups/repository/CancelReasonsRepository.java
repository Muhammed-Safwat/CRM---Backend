package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.entity.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CancelReasonsRepository extends BaseLookupRepository<CancelReasons> {

    @Query("SELECT cr.name FROM CancelReasons cr WHERE cr.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}
