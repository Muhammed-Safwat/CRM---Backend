package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.entity.PreLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PreLeadRepository extends JpaRepository<PreLead, Long>, JpaSpecificationExecutor<PreLead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PreLead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);
}
