package com.gws.crm.core.employee.repository;

import com.gws.crm.core.employee.entity.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {
}
