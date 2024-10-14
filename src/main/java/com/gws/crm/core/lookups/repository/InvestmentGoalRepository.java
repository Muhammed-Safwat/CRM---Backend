package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.InvestmentGoal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentGoalRepository extends BaseLookupRepository<InvestmentGoal> {

    @Query("SELECT ig.name FROM InvestmentGoal ig WHERE ig.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}
