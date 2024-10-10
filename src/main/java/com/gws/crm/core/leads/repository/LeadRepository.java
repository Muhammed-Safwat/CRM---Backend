package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.Lead;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {

    @Modifying
    @Transactional
    @Query("UPDATE Lead l SET l.deleted = true WHERE l.id = :leadId")
    void deleteLead(@Param("leadId") long leadId);
    @Modifying
    @Transactional
    @Query("UPDATE Lead l SET l.deleted = false WHERE l.id = :leadId")
    void restoreLead(Long leadId);
}
