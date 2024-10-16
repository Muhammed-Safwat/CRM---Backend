package com.gws.crm.core.lookups.repository;


import com.gws.crm.core.lookups.entity.PropertyType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyTypeRepository extends BaseLookupRepository<PropertyType> {

    @Query("SELECT pt.name FROM PropertyType pt  WHERE pt.admin.id = :adminId")
    List<String> findAllNamesByAdminId(@Param("adminId") Long adminId);

}
