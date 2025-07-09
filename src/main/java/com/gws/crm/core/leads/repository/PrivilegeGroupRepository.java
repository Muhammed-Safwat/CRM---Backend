package com.gws.crm.core.leads.repository;

import com.gws.crm.authentication.dto.PrivilegeGroupDTO;
import com.gws.crm.authentication.entity.PrivilegeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrivilegeGroupRepository extends JpaRepository<PrivilegeGroup, Long> , JpaSpecificationExecutor<PrivilegeGroup> {

    @Query("SELECT pg.id AS id , pg.jobName AS jobName FROM PrivilegeGroup pg")
    List<PrivilegeGroupDTO> getAllPrivilegeGroups();
}
