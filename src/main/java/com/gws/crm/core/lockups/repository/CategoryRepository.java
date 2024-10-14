package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByAdminId(Pageable pageable, Long userId);

    List<Category> findAllByAdminId(Long userId);

    Optional<Category> findByIdAndAdminId(long id, long admin_id);

    Category findByNameAndAdminId(String name, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);

    long countByAdminId(long id);
}
