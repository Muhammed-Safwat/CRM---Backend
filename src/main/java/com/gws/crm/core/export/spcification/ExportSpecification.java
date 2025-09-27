package com.gws.crm.core.export.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.export.dtos.ExportCritira;
import com.gws.crm.core.export.entity.ExportTask;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportSpecification {

    public Specification<ExportTask> filter(ExportCritira criteria, Transition transition) {
        List<Specification<ExportTask>> specs = new ArrayList<>();

        specs.add(fetchData());

        if (criteria != null) {
            specs.add(filterByType(criteria.getType()));
            specs.add(filterByStatus(criteria.getStatus()));
            specs.add(filterByCreatedAt(criteria.getCreatedAt()));
            specs.add(filterByExporter(criteria.getExporterIds()));
            specs.add(fullTextSearch(criteria.getKeyword()));
        }

        return Specification.allOf(specs);
    }

    public   Specification<ExportTask> fetchData() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("exportedBy", JoinType.LEFT);
            }
            query.distinct(true);
            return null;
        };
    }

    private   Specification<ExportTask> filterByType(String type) {
        return (root, query, cb) -> !StringUtils.hasText(type) ? null :
                cb.equal(root.get("type"), type);
    }

    private   Specification<ExportTask> filterByStatus(String status) {
        return (root, query, cb) -> !StringUtils.hasText(status) ? null :
                cb.equal(root.get("status"), status);
    }

    private   Specification<ExportTask> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, cb) -> createdAt == null ? null :
                cb.equal(root.get("createdAt").as(LocalDate.class), createdAt);
    }

    private   Specification<ExportTask> filterByExporter(List<Long> userIds) {
        return (root, query, cb) -> (userIds == null || userIds.isEmpty()) ? null :
                root.join("exportedBy", JoinType.LEFT).get("id").in(userIds);
    }

    private   Specification<ExportTask> fullTextSearch(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) return null;
            String lowerKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("filename")), lowerKeyword),
                    cb.like(cb.lower(root.get("filePath")), lowerKeyword),
                    cb.like(cb.lower(root.get("type")), lowerKeyword)
            );
        };
    }
}
