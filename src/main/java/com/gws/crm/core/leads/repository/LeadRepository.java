package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.Lead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends SalesLeadRepository<Lead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Lead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);
}
