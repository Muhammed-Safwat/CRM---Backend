package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.Lead;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends SalesLeadRepository<Lead> {


}
