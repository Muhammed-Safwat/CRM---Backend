package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.BaseLookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@NoRepositoryBean
public interface BaseLookupRepository<T extends BaseLookup> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    Page<T> findAllByAdminId(Pageable pageable, Long adminId);

    List<T> findAllByAdminId(Long adminId);

    Optional<T> findByIdAndAdminId(Long id, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(Long id, Long userId);

    T findByNameAndAdminId(String name, Long userId);

    Long countByAdminId(Long id);

    T findByName(String status);

}
