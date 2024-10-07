package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.Lead;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long>  , JpaSpecificationExecutor<Lead> {

}
