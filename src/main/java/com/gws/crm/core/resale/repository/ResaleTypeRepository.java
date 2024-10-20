package com.gws.crm.core.resale.repository;

import com.gws.crm.core.resale.entities.ResaleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResaleTypeRepository extends JpaRepository<ResaleType, Long> {
    ResaleType findByName(String type);

    @Query("SELECT rc.name FROM ResaleType rc")
    List<String> findAllNames();
}
