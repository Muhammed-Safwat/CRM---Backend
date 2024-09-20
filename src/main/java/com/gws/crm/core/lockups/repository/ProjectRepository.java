package com.gws.crm.core.lockups.repository;

import com.gws.crm.core.lockups.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
