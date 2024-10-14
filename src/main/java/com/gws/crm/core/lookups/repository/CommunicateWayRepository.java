package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.entity.CommunicateWay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommunicateWayRepository extends BaseLookupRepository<CommunicateWay> {

    @Query("SELECT cw.name FROM CommunicateWay cw WHERE cw.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}
