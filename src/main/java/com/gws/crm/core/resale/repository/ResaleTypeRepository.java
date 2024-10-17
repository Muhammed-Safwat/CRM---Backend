package com.gws.crm.core.resale.repository;

import com.gws.crm.core.resale.entities.ResaleStatus;
import com.gws.crm.core.resale.entities.ResaleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResaleTypeRepository extends JpaRepository<ResaleType, Long> {
    ResaleType findByName(String type);
}
