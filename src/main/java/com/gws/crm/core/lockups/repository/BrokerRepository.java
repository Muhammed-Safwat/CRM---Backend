package com.gws.crm.core.lockups.repository;


import com.gws.crm.core.lockups.entity.Broker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BrokerRepository extends JpaRepository<Broker,Long> {
    Page<Broker> findAllByAdminId(Pageable pageable, Long userId);

    @Query("SELECT b.name FROM Broker b WHERE b.admin.id = :userId")
    List<String> findAllNamesByAdminId(@Param("userId") Long userId);

    List<Broker> findAllByAdminId(Long userId);
    Optional<Broker> findByIdAndAdminId(long id, Long userId);

    @Modifying
    @Transactional
    void deleteByIdAndAdminId(long id, Long userId);
    long countByAdminId(long id);
}
