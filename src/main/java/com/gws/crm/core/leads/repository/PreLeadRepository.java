package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.entity.PreLead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PreLeadRepository extends GenericBaseLeadRepository<PreLead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PreLead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(c.id,  c.name, COUNT(l)) " +
            "FROM Channel c " +
            "LEFT JOIN PreLead l ON l.channel = c AND l.admin.id = :userId " +
            "WHERE c.admin.id = :userId " +
            "GROUP BY c.name")
    List<CountDTO> countAllChannelsWithLeadCountForAdmin(Long userId);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(c.id,c.name, COUNT(l)) " +
            "FROM Channel c " +
            "LEFT JOIN PreLead l ON l.channel = c AND l.admin.id IN :userIds " +
            "WHERE c.admin.id IN :userIds " +
            "GROUP BY c.name")
    List<CountDTO> countAllChannelsWithLeadCountForTeam(Set<Long> userIds);

}
