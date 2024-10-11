package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.LeadStatus;
import com.gws.crm.core.lockups.entity.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LeadStatusRepository extends JpaRepository<LeadStatus,Long> {
    @Query("SELECT ls.name FROM LeadStatus ls")
    List<String> findAllNames();
}
