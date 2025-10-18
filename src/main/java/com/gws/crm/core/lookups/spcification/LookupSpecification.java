package com.gws.crm.core.lookups.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.entity.BaseLookup;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

public class LookupSpecification<T extends BaseLookup> {

    public static <T extends BaseLookup> Specification<T> filter(String keyword, long userId, Transition transition) {
        List<Specification<T>> specs = new ArrayList<>();

        specs.add(fetchRelations());

        if (StringUtils.hasText(keyword)) {
            specs.add(fullTextSearch(keyword));
        }

        if (userId > 0) {
            specs.add(filterByAdminId(userId));
        }

        specs.add(filterByDeletedFalse());

        return Specification.allOf(specs);
    }

    private static <T extends BaseLookup> Specification<T> fetchRelations() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                Fetch<Object, Object> adminFetch = root.fetch("admin", JoinType.LEFT);
                // If BaseLookup has region or other relations, fetch them here too
                // root.fetch("region", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }

    private static <T extends BaseLookup> Specification<T> fullTextSearch(String keyword) {
        return (root, query, cb) -> {
            String lowerKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), lowerKeyword);
        };
    }

    private static <T extends BaseLookup> Specification<T> filterByAdminId(long id) {
        return (root, query, cb) -> cb.equal(root.get("admin").get("id"), id);
    }

    private static <T extends BaseLookup> Specification<T> filterByDeletedFalse() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }
}
