package com.gws.crm.core.resale.specification;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.resale.dto.ResaleCriteria;
import com.gws.crm.core.resale.entities.Resale;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResaleSpecification {

    public static Specification<Resale> filter(ResaleCriteria resaleCriteria, Transition transition) {
        List<Specification<Resale>> specs = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        if (resaleCriteria.getSubordinates() != null) {
            ids.addAll(resaleCriteria.getSubordinates());
        }
        specs.add(fetchData());
        ids.add(transition.getUserId());
        if (resaleCriteria != null) {
            specs.add(fullTextSearch(resaleCriteria.getKeyword()));
            specs.add(filterByDeleted(resaleCriteria.isDeleted()));
            specs.add(filterByCreatedAt(resaleCriteria.getCreatedAt()));
            specs.add(filterByUser(ids, resaleCriteria.isMyLead(), transition));
            specs.add(filterBySalesReps(resaleCriteria.getSalesRep(), transition));
            specs.add(filterByCreators(resaleCriteria.getCreator(), transition));
            specs.add(filterByCategory(resaleCriteria.getCategory()));
            specs.add(filterByProject(resaleCriteria.getProject()));
            specs.add(filterByProperty(resaleCriteria.getProject()));
            specs.add(filterByStatus(resaleCriteria.getStatus()));
            specs.add(filterByType(resaleCriteria.getType()));
            specs.add(filterByDelayed(resaleCriteria.getDelayed()));
        }
        return Specification.allOf(specs);
    }

    public static  Specification<Resale> fetchData() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("project",JoinType.LEFT);
                root.fetch("salesRep",JoinType.LEFT);
                root.fetch("type",JoinType.LEFT);
                root.fetch("status",JoinType.LEFT);
            }
            query.distinct(true);
            return cb.conjunction();
        };
    }

    private static Specification<Resale> filterByDelayed(Boolean delayed) {
        return (root, query, criteriaBuilder) -> {
            if (delayed == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("delay"), delayed);
        };
    }

    private static Specification<Resale> filterByUser(List<Long> ids, boolean isMyLead, Transition transition) {

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (!isMyLead && transition.getRole().equals("USER")) {
                predicate = criteriaBuilder.and(predicate, root.join("salesRep", JoinType.INNER).get("id").in(ids));
            } else if (isMyLead && transition.getRole().equals("ADMIN")) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.isNull(root.get("salesRep"))
                );
            } else if (isMyLead && transition.getRole().equals("USER")) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("salesRep").get("id"), transition.getUserId())
                );
            }
            return predicate;
        };
    }

    private static Specification<Resale> filterBySalesReps(List<Long> salesReps, Transition transition) {
        return (root, query, criteriaBuilder) -> {
            if (salesReps == null || salesReps.isEmpty()) {
                return null;
            }

            return root.join("salesRep", JoinType.INNER).get("id").in(salesReps);
        };
    }

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

    private static Specification<Resale> filterByCreators(List<Long> creatorId, Transition transition) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return null;
            }
            return root.join("creator", JoinType.INNER).get("id").in(creatorId);
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
