package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Channel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends BaseLookupRepository<Channel> {

    @Query("SELECT c.name FROM Channel c WHERE c.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}
