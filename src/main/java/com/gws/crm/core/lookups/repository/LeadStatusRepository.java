package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeadStatusRepository extends JpaRepository<LeadStatus, Long> {

    @Query("SELECT ls.name FROM LeadStatus ls")
    List<String> findAllNames();

    LeadStatus findByName(String status);
}
