package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.CancelReasons;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CancelReasonsRepository extends JpaRepository<CancelReasons, Long> {

    Page<CancelReasons> findAllByAdminId(Pageable pageable, Long userId);

    List<CancelReasons> findAllByAdminId(Long userId);

    @Query("SELECT c.name FROM CancelReasons c WHERE c.admin.id = :userId")
    List<String> findAllNamesByAdminId(@Param("userId") Long userId);

    Optional<CancelReasons> findByIdAndAdminId(long id, Long userId);

    CancelReasons findByNameAndAdminId(String name, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);

    long countByAdminId(long id);
}
