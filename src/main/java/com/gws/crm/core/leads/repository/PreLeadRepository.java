package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.PreLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PreLeadRepository extends JpaRepository<PreLead,Long> , JpaSpecificationExecutor<PreLead> {
}
