package com.gws.crm.authentication.repository;

import com.gws.crm.authentication.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByName(String name);


    // List<Privilege> saveAllByName(List<Privilege> privileges);
}
