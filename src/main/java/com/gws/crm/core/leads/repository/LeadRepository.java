package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.entity.Lead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LeadRepository extends GenericSalesLeadRepository<Lead> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Lead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
    boolean isPhoneExist(String phone);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(st.id, st.name, COUNT(s)) " +
            "FROM Stage st " +
            "LEFT JOIN Lead s ON s.stage = st AND s.admin.id = :userId where st.admin.id = :userId " +
            "GROUP BY st.id, st.name")
    List<CountDTO> countAllStagesWithLeadCountForAdmin(Long userId);

    @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(st.id, st.name, COUNT(s)) " +
            "FROM Stage st " +
            "LEFT JOIN SalesLead s ON s.stage = st AND s.salesRep.id IN :userIds " +
            "WHERE st.admin.id IN :userIds " +
            "GROUP BY st.id, st.name")
    List<CountDTO> countAllStagesWithLeadCountForTeam(@Param("userIds") Set<Long> userIds);

    @Query("SELECT l FROM Lead l " +
            "LEFT JOIN FETCH l.status " +
            "LEFT JOIN FETCH l.project " +
            "WHERE l.id IN :ids")
    List<Lead> findAllBasic(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT l FROM Lead l " +
            "LEFT JOIN FETCH l.phoneNumbers " +
            "WHERE l.id IN :ids")
    List<Lead> findAllWithPhones(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT l FROM Lead l " +
            "LEFT JOIN FETCH l.actions " +
            "WHERE l.id IN :ids")
    List<Lead> findAllWithActions(@Param("ids") List<Long> ids);

}
