package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Page<Region> findAllByAdminId(Pageable pageable, Long userId);

    Optional<Region> findByIdAndAdminId(long id, Long userId);
    Region findByNameAndAdminId(String name, Long userId);
    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);

    long countByAdminId(long id);
}
