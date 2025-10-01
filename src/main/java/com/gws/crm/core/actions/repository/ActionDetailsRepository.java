package com.gws.crm.core.actions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gws.crm.core.actions.entity.LeadActionDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionDetailsRepository extends JpaRepository<LeadActionDetails, Long> {

  @Query("""
          SELECT ua FROM LeadActionDetails ua
          WHERE ua.userAction.admin.id = :adminId
      """)
  Page<LeadActionDetails> findActionByAdmin(@Param("adminId") Long adminId, Pageable pageable);

}
