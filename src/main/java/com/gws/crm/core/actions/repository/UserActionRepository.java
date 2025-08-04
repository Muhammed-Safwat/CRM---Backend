package com.gws.crm.core.actions.repository;


import com.gws.crm.core.actions.entity.UserAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    @Query("""
                SELECT ua FROM UserAction ua
                WHERE ua.leadDetails.lead.id = :leadId
                ORDER BY ua.createdAt ASC
            """)
    List<UserAction> findActionsByLeadId(@Param("leadId") Long leadId);

    @Query("""
                SELECT ua FROM UserAction ua
                WHERE ua.leadDetails.lead.id = :leadId
            """)
    Page<UserAction> findActionsByLeadId(@Param("leadId") Long leadId, Pageable pageable);


    Page<UserAction> findAllByCreatorId(@Param("creatorId") Long creatorId, Pageable pageable);


}
