package com.gws.crm.core.resale.repository;

import com.gws.crm.core.resale.entities.ResaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResaleStatusRepository extends JpaRepository<ResaleStatus, Long> {
}