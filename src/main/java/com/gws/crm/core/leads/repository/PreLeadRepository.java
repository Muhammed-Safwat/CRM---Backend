package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.PreLead;
import org.springframework.data.jpa.repository.Query;

public interface PreLeadRepository extends GenericBaseLeadRepository<PreLead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PreLead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);

}
