package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.TeleSalesLead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeleSalesLeadRepository extends GenericSalesLeadRepository<TeleSalesLead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM TeleSalesLead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);
}
