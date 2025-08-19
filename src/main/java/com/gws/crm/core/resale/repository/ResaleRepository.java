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


/*
    @Query("SELECT r FROM Resale r " +
            "LEFT JOIN FETCH r.status " +
            "LEFT JOIN FETCH r.type " +
            "LEFT JOIN FETCH r.project p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH r.creator " +
            "LEFT JOIN FETCH r.salesRep sr " +
            "WHERE r.id = :id")

    @EntityGraph(attributePaths = {
            "status",
            "type",
            "project.category",
            "creator",
            "salesRep"
    })
    @Query("SELECT r FROM Resale r WHERE r.id = :id")
    Optional<Resale> findByIdWithAllRelations(@Param("id") Long id);

 */
}
