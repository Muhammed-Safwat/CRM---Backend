package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.dto.SalesRepCountDTO;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeleSalesLeadRepository extends GenericSalesLeadRepository<TeleSalesLead> {

        @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM TeleSalesLead l JOIN l.phoneNumbers p WHERE p.phone = :phone")
        boolean isPhoneExist(String phone);

        @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(st.id, st.name, COUNT(s)) " +
                        "FROM Stage st " +
                        "LEFT JOIN TeleSalesLead s ON s.stage = st AND s.admin.id = :userId where st.admin.id = :userId "
                        +
                        "GROUP BY st.id, st.name")
        List<CountDTO> countAllStagesWithLeadCountForAdmin(Long userId);

        @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(st.id, st.name, COUNT(s)) " +
                        "FROM Stage st " +
                        "LEFT JOIN TeleSalesLead s ON s.stage = st AND s.salesRep.id IN :userIds " +
                        "WHERE st.admin.id IN :userIds " +
                        "GROUP BY st.id, st.name")
        List<CountDTO> countAllStagesWithLeadCountForTeam(@Param("userIds") Set<Long> userIds);

        @Query("SELECT new com.gws.crm.core.leads.dto.SalesRepCountDTO(e.id, e.name, e.image, COUNT(s)) " +
                        "FROM Employee e " +
                        "LEFT JOIN TeleSalesLead s ON s.salesRep = e AND s.admin.id = :userId AND s.deleted = false " +
                        "WHERE e.admin.id = :userId " +
                        "GROUP BY e.id, e.name, e.image " +
                        "ORDER BY COUNT(s) DESC")
        List<SalesRepCountDTO> countAllSalesRepsWithLeadCountForAdmin(@Param("userId") Long userId);

        @Query("SELECT new com.gws.crm.core.leads.dto.SalesRepCountDTO(e.id, e.name, e.image, COUNT(s)) " +
                        "FROM Employee e " +
                        "LEFT JOIN TeleSalesLead s ON s.salesRep = e AND s.salesRep.id IN :userIds AND s.deleted = false "
                        +
                        "WHERE e.id IN :userIds " +
                        "GROUP BY e.id, e.name, e.image " +
                        "ORDER BY COUNT(s) DESC")
        List<SalesRepCountDTO> countAllSalesRepsWithLeadCountForTeam(@Param("userIds") Set<Long> userIds);
}
