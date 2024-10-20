package com.gws.crm.core.resale.specification;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.resale.dto.ResaleCriteria;
import com.gws.crm.core.resale.entities.Resale;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResaleSpecification {

    public static Specification<Resale> filter(ResaleCriteria resaleCriteria, Transition transition) {
        List<Specification<Resale>> specs = new ArrayList<>();

        if (resaleCriteria != null) {
            specs.add(fullTextSearch(resaleCriteria.getKeyword()));
            specs.add(filterByDeleted(resaleCriteria.isDeleted()));
            specs.add(filterByCreatedAt(resaleCriteria.getCreatedAt()));
            specs.add(filterByUser(resaleCriteria, transition));
            specs.add(filterByCreator(resaleCriteria.getCreator()));
            specs.add(filterByCategory(resaleCriteria.getCategory()));
            specs.add(filterByProject(resaleCriteria.getProject()));
            specs.add(filterByProperty(resaleCriteria.getProject()));
            specs.add(filterByStatus(resaleCriteria.getStatus()));
            specs.add(filterByType(resaleCriteria.getType()));
        }
        return Specification.allOf(specs);
    }

    private static Specification<Resale> filterByUser(ResaleCriteria resaleCriteria, Transition transition) {
        if (resaleCriteria.isMyLead()) {
            return filterByCreator(List.of(transition.getUserId()));
        }
        return filterByAdminId(transition.getUserId());
    }

    // Full text search specification
    private static Specification<Resale> fullTextSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction();
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("BUA")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phase")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), lowerKeyword)

            );
        };
    }


    private static Specification<Resale> filterByCreator(List<Long> creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("creator", JoinType.INNER).get("id").in(creatorId);
        };
    }

    private static Specification<Resale> filterByStatus(List<Long> statusesIds) {
        return (root, query, criteriaBuilder) -> {
            if (statusesIds == null || statusesIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("status", JoinType.INNER).get("id").in(statusesIds);
        };
    }

    private static Specification<Resale> filterByType(Long typeId) {
        return (root, query, criteriaBuilder) -> {
            if (typeId == null || typeId == 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type").get("id"), typeId);
        };
    }

    private static Specification<Resale> filterByCategory(List<Long> creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("category", JoinType.INNER).get("id").in(creatorId);
        };
    }

    private static Specification<Resale> filterByProperty(List<Long> creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("property", JoinType.INNER).get("id").in(creatorId);
        };
    }

    private static Specification<Resale> filterByProject(List<Long> creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("project", JoinType.INNER).get("id").in(creatorId);
        };
    }


    private static Specification<Resale> filterByDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    private static Specification<Resale> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, criteriaBuilder) -> {
            if (createdAt == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("createdAt"), createdAt);
        };
    }

    private static Specification<Resale> filterByAdminId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id == 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("admin").get("id"), id);
        };
    }


}
