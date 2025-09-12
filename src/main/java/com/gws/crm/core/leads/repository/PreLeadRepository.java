package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.entity.PreLead;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PreLeadRepository extends GenericBaseLeadRepository<PreLead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PreLead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(c.id,c.name, COUNT(l)) " +
            "FROM Lead l LEFT JOIN l.channel c " +
            "WHERE l.admin.id = :userId " +
            "GROUP BY c.name")
    List<CountDTO> countLeadsByChannelForAdmin(Long userId);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(c.id,c.name, COUNT(l)) " +
            "FROM Lead l LEFT JOIN l.channel c " +
            "WHERE l.admin.id IN :userIds " +
            "GROUP BY c.name")
    List<CountDTO> countLeadsByChannelForTeam(Set<Long> userIds);

}
