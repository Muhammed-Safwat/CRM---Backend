package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.SalesLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NonGenericLeadRepository extends JpaRepository<SalesLead, Long>,
        JpaSpecificationExecutor<SalesLead> {

    @Modifying
    @Transactional
    @Query("UPDATE SalesLead l SET l.deleted = true WHERE l.id = :leadId")
    void deleteLead(@Param("leadId") long leadId);

    @Modifying
    @Transactional
    @Query("UPDATE SalesLead l SET l.deleted = false WHERE l.id = :leadId")
    void restoreLead(Long leadId);
}
