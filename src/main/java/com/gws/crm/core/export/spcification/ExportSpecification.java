package com.gws.crm.core.export.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.export.dtos.ExportCriteria;
import com.gws.crm.core.export.entity.ExportTask;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportSpecification {

    public Specification<ExportTask> filter(ExportCriteria criteria, Transition transition) {
        List<Specification<ExportTask>> specs = new ArrayList<>();

        specs.add(fetchData());

        if (criteria != null) {
            specs.add(filterByType(criteria.getType()));
            specs.add(filterByStatus(criteria.getStatus()));
            specs.add(filterByCreatedAt(criteria.getCreatedAt()));
            specs.add(fullTextSearch(criteria.getKeyword()));
            specs.add(filterByAdmin(transition.getUserId()));
            specs.add(filterByReferenceType(criteria.getReferenceType()));
        }

        return Specification.allOf(specs);
    }

    public Specification<ExportTask> fetchData() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("exportedBy", JoinType.LEFT);
            }
            query.distinct(true);
            return null;
        };
    }

    private Specification<ExportTask> filterByType(String type) {
        return (root, query, cb) -> !StringUtils.hasText(type) ? null : cb.equal(root.get("type"), type);
    }

    private Specification<ExportTask> filterByStatus(String status) {
        return (root, query, cb) -> !StringUtils.hasText(status) ? null : cb.equal(root.get("status"), status);
    }

    private Specification<ExportTask> filterByCreatedAt(List<LocalDateTime> createdAt) {
        return (root, query, cb) -> {
            if (createdAt != null && createdAt.size() == 2) {
                return cb.between(root.get("createdAt"), createdAt.get(0), createdAt.get(1));
            } else if (createdAt != null && createdAt.size() == 1) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), createdAt.get(0));
            } else {
                return null;
            }
        };
    }

    private Specification<ExportTask> filterByAdmin(Long adminId) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("exportedBy").get("id"), adminId);
        });
    }

    private Specification<ExportTask> fullTextSearch(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword))
                return null;
            String lowerKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("filename")), lowerKeyword),
                    cb.like(cb.lower(root.get("filePath")), lowerKeyword),
                    cb.like(cb.lower(root.get("type")), lowerKeyword));
        };
    }

    private Specification<ExportTask> filterByReferenceType(String referenceType) {
        return (root, query, cb) -> !StringUtils.hasText(referenceType) ? null
                : cb.equal(root.get("referenceType"), referenceType);
    }
}
