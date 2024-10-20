package com.gws.crm.core.resale.repository;

import com.gws.crm.core.resale.entities.ResaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResaleStatusRepository extends JpaRepository<ResaleStatus, Long> {

    ResaleStatus findByName(String status);

    @Query("SELECT rs.name FROM ResaleStatus rs")
    List<String> findAllNames();
}
