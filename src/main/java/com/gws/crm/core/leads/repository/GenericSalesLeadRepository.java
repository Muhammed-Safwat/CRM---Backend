package com.gws.crm.core.leads.repository;

import com.gws.crm.core.leads.dto.CountDTO;
import com.gws.crm.core.leads.dto.SalesRepCountDTO;
import com.gws.crm.core.leads.entity.SalesLead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface GenericSalesLeadRepository<T extends SalesLead> extends GenericBaseLeadRepository<T> {
        @Modifying
        @Transactional
        @Query("UPDATE SalesLead l SET l.deleted = true WHERE l.id = :leadId")
        void deleteLead(@Param("leadId") long leadId);

        @Modifying
        @Transactional
        @Query("UPDATE SalesLead l SET l.deleted = false WHERE l.id = :leadId")
        void restoreLead(Long leadId);

        Page<T> findAllByAdminId(Pageable pageable, Long userId);

        @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(st.id, st.name, COUNT(s)) " +
                        "FROM Stage st " +
                        "LEFT JOIN SalesLead s ON s.stage = st AND s.admin.id = :userId where st.admin.id = :userId " +
                        "GROUP BY st.id, st.name")
        List<CountDTO> countAllStagesWithLeadCountForAdmin(Long userId);

        @Query("SELECT new com.gws.crm.core.leads.dto.CountDTO(st.id, st.name, COUNT(s)) " +
                        "FROM Stage st " +
                        "LEFT JOIN SalesLead s ON s.stage = st AND s.salesRep.id IN :userIds " +
                        "WHERE st.admin.id IN :userIds " +
                        "GROUP BY st.id, st.name")
        List<CountDTO> countAllStagesWithLeadCountForTeam(@Param("userIds") Set<Long> userIds);

        @Query("SELECT new com.gws.crm.core.leads.dto.SalesRepCountDTO(e.id, e.name, e.image, COUNT(s)) " +
                        "FROM Employee e " +
                        "LEFT JOIN SalesLead s ON s.salesRep = e AND s.admin.id = :userId AND s.deleted = false " +
                        "WHERE e.admin.id = :userId " +
                        "GROUP BY e.id, e.name, e.image " +
                        "ORDER BY COUNT(s) DESC")
        List<SalesRepCountDTO> countAllSalesRepsWithLeadCountForAdmin(@Param("userId") Long userId);


}
