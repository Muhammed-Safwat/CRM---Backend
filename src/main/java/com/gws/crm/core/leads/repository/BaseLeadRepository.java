package com.gws.crm.core.leads.repository;


import com.gws.crm.core.leads.entity.BaseLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BaseLeadRepository extends JpaRepository<BaseLead, Long>, JpaSpecificationExecutor<BaseLead> {

    @Query("""
                SELECT l FROM BaseLead l
                WHERE 
                    (l.createdAt <= :threshold AND l.actions IS EMPTY AND l.nextActionDate IS NULL)
                    OR 
                    (l.nextActionDate IS NOT NULL AND l.nextActionDate < CURRENT_TIMESTAMP)
            """)
    List<BaseLead> findAllDelayed(@Param("threshold") LocalDateTime threshold);


}
