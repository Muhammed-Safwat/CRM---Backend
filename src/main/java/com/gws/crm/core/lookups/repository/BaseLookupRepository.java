package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.BaseLookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseLookupRepository<T extends BaseLookup>
        extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    Page<T> findAllByAdminIdAndDeletedFalse(Pageable pageable, Long adminId);

    List<T> findAllByAdminIdAndDeletedFalse(Long adminId);

    Optional<T> findByIdAndAdminId(Long id, Long userId);

    Optional<T> findByNameAndAdminId(String name, Long userId);

    Long countByAdminIdAndDeletedFalse(Long id);

    Optional<T> findByName(String status);
}
