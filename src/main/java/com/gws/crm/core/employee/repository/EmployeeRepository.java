package com.gws.crm.core.employee.repository;

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
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Set<Employee> findAllByAdminId(long adminId);

    @Query("SELECT e FROM Employee e WHERE e.id IN :ids")
    Set<Employee> findAllEmpById(@Param("ids") List<Long> ids);


    @Query("SELECT e.name FROM Employee e WHERE e.admin.id = :userId")
    List<String> findAllNamesByAdminId(@Param("userId") Long userId);

    Optional<Employee> getByIdAndAdminId(long id, long adminId);

    @Query("""
            SELECT DISTINCT e
            FROM Employee e
            LEFT JOIN FETCH  e.subordinates s
            LEFT JOIN FETCH  e.privileges  p
            WHERE e.id= :id and e.admin.id = :adminId
            """)
    Optional<Employee> getByIdAndAdminIdWithRelations(long id, long adminId);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE e.id = :id and e.admin.id = :adminId")
    void deleteByIdAndAdminId(long id, long adminId);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE e.id = :id and e.admin.id = :adminId")
    void restoreByIdAndAdminId(long id, long adminId);

    Employee findByNameAndAdminId(String salesRep, long id);

    Set<Employee> findAllByAdminIdAndJobNameIn(Long adminId, List<String> jobNames);

    long countByPrivilegeGroupIdAndAdminId(long id, long adminId);

    @Query("SELECT e FROM Employee e WHERE e.privilegeGroup.id =:id")
    Set<Employee> getEmployeesByPrivilegeGroupId(@Param("id") long id);

    @Query(value = "SELECT COUNT(*) > 0 FROM employee_subordinates " +
            "WHERE manager_id = :managerId AND subordinate_id = :employeeId", nativeQuery = true)
    long isSubordinate(@Param("managerId") Long managerId, @Param("employeeId") Long employeeId);


    @Query("select e from Employee e left join fetch e.subordinates where e.id = :id")
    Optional<Employee> findWithSubordinatesById(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT e
            FROM Employee e
            WHERE e.admin.id = :adminId
              AND e.enabled = true
              AND e.deleted = false
              AND e.locked = false
            """)
    List<Employee> getEmployeesByAdminId(@Param("adminId") long adminId);

    @Query("""
            SELECT DISTINCT e 
            FROM Employee e
             LEFT JOIN FETCH e.subordinates s
            WHERE e.admin.id = :adminId 
            """)
    Optional<Employee> findByIdWithSubordinates(long adminId);
}
