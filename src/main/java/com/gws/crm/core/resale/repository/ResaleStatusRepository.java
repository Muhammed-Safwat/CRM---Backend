package com.gws.crm.core.resale.repository;

import com.gws.crm.core.resale.entities.ResaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResaleStatusRepository extends JpaRepository<ResaleStatus, Long> {

    ResaleStatus findByName(String status);
}
