package com.gws.crm.core.lookups.repository;

import com.gws.crm.core.lookups.dto.SimpleProjectDto;
import com.gws.crm.core.lookups.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"region", "category", "devCompany", "admin"})
    @Query("select p from Project p where p.admin.id = :adminId")
    Page<Project> findAllByAdminId(Pageable pageable, @Param("adminId") Long adminId);

    @EntityGraph(attributePaths = {"region", "category", "devCompany", "admin"})
    @Query("""
       select p from Project p
       where p.admin.id = :adminId
         and (:keyword is null or lower(p.name) like lower(concat('%', :keyword, '%'))) and p.deleted = false
       """)
    Page<Project> findAllByAdminIdAndKeyword(Pageable pageable,
                                   @Param("adminId") Long adminId,
                                   @Param("keyword") String keyword);


    @Query("SELECT p from Project p where p.admin.id = :userId and p.deleted = false")
    List<Project> findAllByAdminId(Long userId);

    @Query("SELECT p.name FROM Project p WHERE p.admin.id = :userId and p.deleted = false")
    List<String> findAllNamesByAdminId(@Param("userId") Long userId);

    Project findByNameAndAdminId(String name, Long userId);

    long countByAdminIdAndDeletedFalse(long id);

    @Query("SELECT new com.gws.crm.core.lookups.dto.SimpleProjectDto(p.id,p.name) " +
            "FROM Project p " +
            "WHERE p.admin.id = :id and p.deleted = false")
    List<SimpleProjectDto> findSimpleProjectData(long id);

    @Modifying
    @Transactional
    @Query("update Project p set p.deleted = true where p.id = :id")
    void deleteProject(@Param("id") long id);


}
