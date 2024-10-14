package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.PreLead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreLeadRepository extends JpaRepository<PreLead,Long> {
}
