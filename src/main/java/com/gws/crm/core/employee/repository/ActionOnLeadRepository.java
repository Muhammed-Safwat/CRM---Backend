package com.gws.crm.core.employee.repository;

import com.gws.crm.core.employee.entity.ActionOnLead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionOnLeadRepository extends JpaRepository<ActionOnLead, Long> {

    List<ActionOnLead> getAllByLeadIdOrderByCreatedAtAsc(long leadId);

}
