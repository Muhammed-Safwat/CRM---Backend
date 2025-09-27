package com.gws.crm.core.export.repository;

import com.gws.crm.core.export.entity.ExportStatus;
import com.gws.crm.core.export.entity.ExportTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportTaskRepository extends JpaRepository<ExportTask , Long> , JpaSpecificationExecutor<ExportTask> {
    List<ExportTask> findByStatus(ExportStatus exportStatus);
}
