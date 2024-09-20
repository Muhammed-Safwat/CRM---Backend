package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
