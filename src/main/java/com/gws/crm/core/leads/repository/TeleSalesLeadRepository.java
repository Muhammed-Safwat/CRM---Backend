package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeleSalesLeadRepository extends GenericSalesLeadRepository<TeleSalesLead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM TeleSalesLead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(s.id,s.stage.name, COUNT(s)) " +
            "FROM TeleSalesLead s WHERE s.admin.id = :userId GROUP BY s.stage.name")
    List<CountDTO> countLeadsByStageForAdmin(Long userId);
}
