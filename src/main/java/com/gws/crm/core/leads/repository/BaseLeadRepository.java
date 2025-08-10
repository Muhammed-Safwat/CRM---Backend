package com.gws.crm.core.leads.repository;


import com.gws.crm.core.leads.entity.BaseLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BaseLeadRepository extends JpaRepository<BaseLead, Long>, JpaSpecificationExecutor<BaseLead> {

    @Modifying
    @Query("""
                UPDATE Lead l
                SET l.delay = true
                WHERE 
                    l.createdAt <= :thresholdTime 
                    AND (
                        l.nextActionDate IS NULL OR l.nextActionDate <= :now
                    )
                    AND l.delay = false
            """)
    int markDelayedLeads(
            @Param("thresholdTime") LocalDateTime thresholdTime,
            @Param("now") LocalDateTime now
    );

    // دالة لإرجاع الـ delayed leads
    @Query("""
            SELECT l FROM Lead l
            WHERE 
                l.createdAt <= :thresholdTime 
                AND (
                    l.nextActionDate IS NULL OR l.nextActionDate <= :now
                )
                AND l.delay = true
            """)
    List<BaseLead> findDelayedLeads(
            @Param("thresholdTime") LocalDateTime thresholdTime,
            @Param("now") LocalDateTime now
    );

    // دالة لإرجاع الـ leads القريبة من الـ delay
    @Query("""
            SELECT l FROM Lead l
            WHERE 
                l.createdAt BETWEEN :createdAfter AND :warningThreshold
                AND l.delay = false
                AND (
                    l.nextActionDate IS NULL OR l.nextActionDate <= :now
                )
            """)
    List<BaseLead> findLeadsNearingDelay(
            @Param("createdAfter") LocalDateTime createdAfter,
            @Param("warningThreshold") LocalDateTime warningThreshold,
            @Param("now") LocalDateTime now
    );
}
