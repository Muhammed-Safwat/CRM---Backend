package com.gws.crm.core.employee.repository;

import com.gws.crm.core.employee.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    @Query("""
            SELECT DISTINCT a
            FROM Admin a
            LEFT JOIN FETCH a.company c """)
    List<Admin> findAllWithCompany();

    @Query("""
            SELECT  a
            FROM Admin a
            LEFT JOIN FETCH a.employees e
            where a.id= :userId""")
    Admin findByIdWithEmployees(Long userId);
}
