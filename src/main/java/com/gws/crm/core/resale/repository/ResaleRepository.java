package com.gws.crm.core.resale.repository;


import com.gws.crm.core.resale.entities.Resale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ResaleRepository extends JpaRepository<Resale, Long>, JpaSpecificationExecutor<Resale> {
}
