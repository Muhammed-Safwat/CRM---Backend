package com.gws.crm.core.actions.repository;

import com.gws.crm.core.actions.entity.LeadActionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadActionDetailsRepository extends JpaRepository<LeadActionDetails, Long> {

}
