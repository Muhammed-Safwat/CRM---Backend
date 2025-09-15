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
        List<Long> ids = new ArrayList<>();
        if (resaleCriteria.getSubordinates() != null) {
            ids.addAll(resaleCriteria.getSubordinates());
        }
        specs.add(fetchData());
        ids.add(transition.getUserId());

        if (resaleCriteria != null) {
            specs.add(filterByChannel(resaleCriteria.getChannel()));
            specs.add(fullTextSearch(resaleCriteria.getKeyword()));
            specs.add(filterByDeleted(resaleCriteria.isDeleted()));
            specs.add(filterByCreatedAt(resaleCriteria.getCreatedAt()));
            specs.add(filterByUser(ids, resaleCriteria.isMyLead(), transition));
            specs.add(filterBySalesReps(resaleCriteria.getSalesRep()));
            specs.add(filterByCreators(resaleCriteria.getCreator()));
            specs.add(filterByCategory(resaleCriteria.getCategory()));
            specs.add(filterByProject(resaleCriteria.getProject()));
            specs.add(filterByProperty(resaleCriteria.getProperty()));
            specs.add(filterByStatus(resaleCriteria.getStatus()));
            specs.add(filterByType(resaleCriteria.getType()));
            specs.add(filterByDelayed(resaleCriteria.getDelayed()));
        }

        return Specification.allOf(specs);
    }

    public static Specification<Resale> fetchData() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("project", JoinType.LEFT);
                root.fetch("salesRep", JoinType.LEFT);
                root.fetch("type", JoinType.LEFT);
                root.fetch("status", JoinType.LEFT);
            }
            query.distinct(true);
            return null;
        };
    }

    private static Specification<Resale> filterByUser(List<Long> ids, boolean isMyLead, Transition transition) {
        return (root, query, cb) -> {
            if (!isMyLead && "USER".equals(transition.getRole()) && !ids.isEmpty()) {
                return root.join("salesRep", JoinType.INNER).get("id").in(ids);
            } else if (isMyLead && "ADMIN".equals(transition.getRole())) {
                return cb.isNull(root.get("salesRep"));
            } else if (isMyLead && "USER".equals(transition.getRole())) {
                return cb.equal(root.get("salesRep").get("id"), transition.getUserId());
            }
            return null;
        };
    }

    private static Specification<Resale> filterBySalesReps(List<Long> salesReps) {
        return (root, query, cb) -> (salesReps == null || salesReps.isEmpty()) ? null :
                root.join("salesRep", JoinType.INNER).get("id").in(salesReps);
    }

    private static Specification<Resale> fullTextSearch(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) return null;
            String lowerKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), lowerKeyword),
                    cb.like(cb.lower(root.get("BUA")), lowerKeyword),
                    cb.like(cb.lower(root.get("phase")), lowerKeyword),
                    cb.like(cb.lower(root.get("code")), lowerKeyword),
                    cb.like(cb.lower(root.get("country")), lowerKeyword),
                    cb.like(cb.lower(root.get("phone")), lowerKeyword)
            );
        };
    }

    private static Specification<Resale> filterByCreators(List<Long> creatorIds) {
        return (root, query, cb) -> (creatorIds == null || creatorIds.isEmpty()) ? null :
                root.join("creator", JoinType.LEFT).get("id").in(creatorIds);
    }

    private static Specification<Resale> filterByProject(List<Long> projectIds) {
        return (root, query, cb) -> (projectIds == null || projectIds.isEmpty()) ? null :
                root.join("project", JoinType.LEFT).get("id").in(projectIds);
    }

    private static Specification<Resale> filterByChannel(List<Long> channelIds) {
        return (root, query, cb) -> (channelIds == null || channelIds.isEmpty()) ? null :
                root.join("channel", JoinType.LEFT).get("id").in(channelIds);
    }
    private static Specification<Resale> filterByProperty(List<Long> propertyIds) {
        return (root, query, cb) -> (propertyIds == null || propertyIds.isEmpty()) ? null :
                root.join("property", JoinType.LEFT).get("id").in(propertyIds);
    }

    private static Specification<Resale> filterByCategory(List<Long> categoryIds) {
        return (root, query, cb) -> (categoryIds == null || categoryIds.isEmpty()) ? null :
                root.join("category", JoinType.LEFT).get("id").in(categoryIds);
    }

    private static Specification<Resale> filterByStatus(List<Long> statusIds) {
        return (root, query, cb) -> (statusIds == null || statusIds.isEmpty()) ? null :
                root.join("status", JoinType.LEFT).get("id").in(statusIds);
    }

    private static Specification<Resale> filterByType(Long typeId) {
        return (root, query, cb) -> (typeId == null || typeId == 0) ? null :
                cb.equal(root.get("type").get("id"), typeId);
    }

    private static Specification<Resale> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, cb) -> (createdAt == null) ? null :
                cb.equal(root.get("createdAt"), createdAt);
    }

    private static Specification<Resale> filterByDeleted(Boolean deleted) {
        return (root, query, cb) -> (deleted == null) ? null :
                cb.equal(root.get("deleted"), deleted);
    }

    private static Specification<Resale> filterByDelayed(Boolean delayed) {
        return (root, query, cb) -> (delayed == null) ? null :
                cb.equal(root.get("delay"), delayed);
    }

    private static Specification<Resale> filterByAdminId(Long id) {
        return (root, query, cb) -> (id == null || id == 0) ? null :
                cb.equal(root.get("admin").get("id"), id);
    }
}
