package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.LeadStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadStatusRepository extends BaseLookupRepository<LeadStatus> {

    @Query("SELECT ls.name FROM LeadStatus ls")
    List<String> findAllNames();

}
