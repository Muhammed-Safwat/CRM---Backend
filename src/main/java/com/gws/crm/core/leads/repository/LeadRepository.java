package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.entity.Lead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends GenericSalesLeadRepository<Lead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Lead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(s.stage.id, s.stage.name, COUNT(s)) \n" +
            "FROM Lead s \n" +
            "WHERE s.admin.id = :userId \n" +
            "GROUP BY s.stage.id, s.stage.name\n")
    List<CountDTO> countLeadsByStageForAdmin(Long userId);


}
