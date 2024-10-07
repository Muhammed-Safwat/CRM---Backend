package com.gws.crm.core.employee.repository;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Page<Employee> findAllByAdminId(long adminId, Pageable pageable);
    List<Employee> findAllByAdminId(long adminId);

    Optional<Employee> getByIdAndAdminId(long id, long adminId);

    @Transactional
    @Modifying
    void deleteByIdAndAdminId(long id, long adminId);
}
