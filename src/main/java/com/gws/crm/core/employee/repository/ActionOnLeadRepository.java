package com.gws.crm.core.employee.repository;

import com.gws.crm.core.employee.entity.ActionOnLead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActionOnLeadRepository extends JpaRepository<ActionOnLead,Long> {

    List<ActionOnLead> getAllByLeadIdAndOrderByCreatedAtDesc(long leadId);
}
