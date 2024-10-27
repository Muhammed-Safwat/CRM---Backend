package com.gws.crm.core.admin.repository;

import com.gws.crm.core.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Admin a SET a.deleted = true WHERE a.id = :id")
    void deleteAdminById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Admin a SET a.deleted = false WHERE a.id = :id")
    void restoreAdmin(long id);

}
