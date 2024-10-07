package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, Long> {
    Page<Stage> findAllByAdminId(Pageable pageable, Long userId);

    Optional<Stage> findByIdAndAdminId(long id, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);


    long countByAdminId(long id);
}
