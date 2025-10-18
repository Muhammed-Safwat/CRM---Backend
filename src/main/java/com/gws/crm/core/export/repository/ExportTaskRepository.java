package com.gws.crm.core.export.repository;

import com.gws.crm.common.dto.ExportStatisticsDto;
import com.gws.crm.core.export.entity.ExportStatus;
import com.gws.crm.core.export.entity.ExportTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportTaskRepository extends JpaRepository<ExportTask , Long> , JpaSpecificationExecutor<ExportTask> {
    List<ExportTask> findByStatus(ExportStatus exportStatus);

    @Query("""
       SELECT new com.gws.crm.common.dto.ExportStatisticsDto(
           SUM(CASE WHEN t.status = 'QUEUED' THEN 1 ELSE 0 END),
           SUM(CASE WHEN t.status = 'PROCESSING' THEN 1 ELSE 0 END),
           SUM(CASE WHEN t.status = 'COMPLETED' THEN 1 ELSE 0 END),
           SUM(CASE WHEN t.status = 'FAILED' THEN 1 ELSE 0 END)
       )
       FROM ExportTask t
       WHERE t.exportedBy.id = :exportedById and t.referenceType = :referenceType
       """)
    ExportStatisticsDto countByStatus(String referenceType,long exportedById);

}
