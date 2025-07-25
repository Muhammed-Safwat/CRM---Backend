package com.gws.crm.core.actions.repository.repository;

import com.gws.crm.core.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    List<Employee> findAllByAdminId(long adminId);

    @Query("SELECT e.name FROM Employee e WHERE e.admin.id = :userId")
    List<String> findAllNamesByAdminId(@Param("userId") Long userId);

    Optional<Employee> getByIdAndAdminId(long id, long adminId);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE e.id = :id and e.admin.id = :adminId")
    void deleteByIdAndAdminId(long id, long adminId);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE e.id = :id and e.admin.id = :adminId")
    void restoreByIdAndAdminId(long id, long adminId);

    Employee findByNameAndAdminId(String salesRep, long id);

    List<Employee> findAllByAdminIdAndJobNameIn(Long adminId, List<String> jobNames);

    long countByPrivilegeGroupIdAndAdminId(long id, long adminId);

    @Query("SELECT e FROM Employee e WHERE e.privilegeGroup.id =:id")
    List<Employee> getEmployeesByPrivilegeGroupId(@Param("id") long id);

}
