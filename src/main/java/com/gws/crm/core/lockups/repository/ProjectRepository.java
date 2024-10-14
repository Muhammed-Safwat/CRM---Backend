package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByAdminId(Pageable pageable, Long userId);

    List<Project> findAllByAdminId(Long userId);

    Optional<Project> findByIdAndAdminId(long id, Long userId);

    @Query("SELECT p.name FROM Project p WHERE p.admin.id = :userId")
    List<String> findAllNamesByAdminId(@Param("userId") Long userId);

    Project findByNameAndAdminId(String name, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);

    long countByAdminId(long id);
}
