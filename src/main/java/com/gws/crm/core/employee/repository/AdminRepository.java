package com.gws.crm.core.employee.repository;

import com.gws.crm.core.employee.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    Optional<Admin> findByIdWithEmployees(Long userId);
    @Query("""
            SELECT DISTINCT a
            FROM Admin a
            LEFT JOIN FETCH a.company c where a.id = :adminId""")
    Optional<Admin> findByIdWithCompany(Long adminId);
}
