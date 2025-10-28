package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.dto.SalesRepCountDTO;
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
                        "LEFT JOIN Lead s ON s.stage = st AND s.admin.id = :adminId where st.admin.id = :adminId " +
                        "GROUP BY st.id, st.name")
        List<CountDTO> countAllStagesWithLeadCountForAdmin(Long adminId);

        @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(st.id, st.name, COUNT(s)) " +
                        "FROM Stage st " +
                        "LEFT JOIN SalesLead s ON s.stage = st AND s.salesRep.id = :userId " +
                        "WHERE st.admin.id = :adminId " +
                        "GROUP BY st.id, st.name")
        List<CountDTO> countAllStagesWithLeadCountForEmployee(Long adminId , Long userId);

        @Query("SELECT l FROM Lead l " +
                        "LEFT JOIN FETCH l.status " +
                        "LEFT JOIN FETCH l.project " +
                        "LEFT JOIN FETCH l.channel " +
                        "LEFT JOIN FETCH l.creator " +
                        "LEFT JOIN FETCH l.admin " +
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

        @Query("SELECT DISTINCT l FROM Lead l " +
                        "LEFT JOIN FETCH l.status " +
                        "LEFT JOIN FETCH l.project " +
                        "LEFT JOIN FETCH l.channel " +
                        "LEFT JOIN FETCH l.creator " +
                        "LEFT JOIN FETCH l.admin " +
                        "LEFT JOIN FETCH l.actions " +
                        "LEFT JOIN FETCH l.investmentGoal " +
                        "LEFT JOIN FETCH l.communicateWay " +
                        "LEFT JOIN FETCH l.cancelReasons " +
                        "LEFT JOIN FETCH l.salesRep " +
                        "LEFT JOIN FETCH l.assignFrom " +
                        "LEFT JOIN FETCH l.broker " +
                        "LEFT JOIN FETCH l.stage " +
                        "WHERE l.id IN :ids")
        List<Lead> findAllWithAllRelations(@Param("ids") List<Long> ids);

        @Query("SELECT new com.gws.crm.core.leads.dto.SalesRepCountDTO(e.id, e.name, e.image, COUNT(s)) " +
                        "FROM Employee e " +
                        "LEFT JOIN Lead s ON s.salesRep = e AND s.admin.id = :userId AND s.deleted = false " +
                        "WHERE e.admin.id = :userId " +
                        "GROUP BY e.id, e.name, e.image " +
                        "ORDER BY COUNT(s) DESC")
        List<SalesRepCountDTO> countAllSalesRepsWithLeadCountForAdmin(@Param("userId") Long userId);

        @Query("SELECT new com.gws.crm.core.leads.dto.SalesRepCountDTO(e.id, e.name, e.image, COUNT(s)) " +
                        "FROM Employee e " +
                        "LEFT JOIN Lead s ON s.salesRep = e AND s.salesRep.id IN :userIds AND s.deleted = false " +
                        "WHERE e.id IN :userIds " +
                        "GROUP BY e.id, e.name, e.image " +
                        "ORDER BY COUNT(s) DESC")
        List<SalesRepCountDTO> countAllSalesRepsWithLeadCountForTeam(@Param("userIds") Set<Long> userIds);

}
