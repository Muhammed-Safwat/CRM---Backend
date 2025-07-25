package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.BaseLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface GenericBaseLeadRepository<T extends BaseLead> extends JpaRepository<T, Long>,
        JpaSpecificationExecutor<T> {

    @Modifying
    @Transactional
    @Query("UPDATE BaseLead l SET l.deleted = true WHERE l.id = :leadId")
    void deleteLead(@Param("leadId") long leadId);

    @Modifying
    @Transactional
    @Query("UPDATE BaseLead l SET l.deleted = false WHERE l.id = :leadId")
    void restoreLead(Long leadId);

}
