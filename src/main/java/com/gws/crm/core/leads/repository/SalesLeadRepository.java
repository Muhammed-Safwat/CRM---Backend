package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.SalesLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesLeadRepository<T extends SalesLead> extends JpaRepository<T, Long> {
}
