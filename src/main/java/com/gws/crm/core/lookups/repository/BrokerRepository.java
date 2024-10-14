package com.gws.crm.core.lookups.repository;


import com.gws.crm.core.lookups.entity.Broker;
import com.gws.crm.core.lookups.entity.DevCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrokerRepository extends BaseLookupRepository<Broker> {

    @Query("SELECT b.name FROM Broker b WHERE b.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}