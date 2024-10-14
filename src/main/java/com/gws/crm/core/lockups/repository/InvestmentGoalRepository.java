package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.InvestmentGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InvestmentGoalRepository extends JpaRepository<InvestmentGoal, Long> {

    Page<InvestmentGoal> findAllByAdminId(Pageable pageable, Long userId);

    List<InvestmentGoal> findAllByAdminId(Long userId);

    @Query("SELECT ig.name FROM InvestmentGoal ig WHERE ig.admin.id = :userId")
    List<String> findAllNamesByAdminId(@Param("userId") Long userId);

    Optional<InvestmentGoal> findByIdAndAdminId(long id, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);

    InvestmentGoal findByNameAndAdminId(String name, Long userId);
    long countByAdminId(long id);
}
