package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.InvestmentGoal;
import com.gws.crm.core.lockups.entity.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InvestmentGoalRepository extends JpaRepository<InvestmentGoal ,Long> {

    Page<InvestmentGoal> findAllByAdminId(Pageable pageable, Long userId);
    List<InvestmentGoal> findAllByAdminId(Long userId);
    Optional<InvestmentGoal> findByIdAndAdminId(long id, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);

    long countByAdminId(long id);
}
