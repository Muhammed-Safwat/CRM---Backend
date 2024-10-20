package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.TeleSalesLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleSalesLeadRepository extends JpaRepository<TeleSalesLead, Long>, JpaSpecificationExecutor<TeleSalesLead> {


}
