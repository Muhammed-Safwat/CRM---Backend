package com.gws.crm.core.lookups.repository;


import com.gws.crm.core.lookups.entity.Broker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrokerRepository extends BaseLookupRepository<Broker> {

    @Query("SELECT b.name FROM Broker b WHERE b.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}