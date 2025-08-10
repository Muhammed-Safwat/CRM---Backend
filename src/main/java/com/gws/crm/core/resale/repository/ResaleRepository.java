package com.gws.crm.core.resale.repository;


import com.gws.crm.core.resale.entities.Resale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResaleRepository extends JpaRepository<Resale, Long>, JpaSpecificationExecutor<Resale> {

    @Modifying
    @Transactional
    @Query("UPDATE Resale l SET l.deleted = true WHERE l.id = :resaleId")
    void deleteResale(@Param("resaleId") long resaleId);

    @Modifying
    @Transactional
    @Query("UPDATE Resale l SET l.deleted = false WHERE l.id = :resaleId")
    void restoreResale(@Param("resaleId") long resaleId);

    boolean existsByPhone(String phone);

    @Modifying
    @Query("UPDATE Resale r SET r.archive = :archived WHERE r.id = :resaleId")
    void toggleArchive(long resaleId, boolean archived);

}
