package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends BaseLookupRepository<Category> {

    @Query("SELECT c.name FROM Category c WHERE c.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);
}