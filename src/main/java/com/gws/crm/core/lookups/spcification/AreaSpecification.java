package com.gws.crm.core.lookups.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.entity.Area;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

public class AreaSpecification {

    public static Specification<Area> filter(String keyword, Transition transition) {
        List<Specification<Area>> specs = new ArrayList<>();

        // Always fetch region + admin to avoid N+1
        specs.add(fetchRelations());

        if (StringUtils.hasText(keyword)) {
            specs.add(fullTextSearch(keyword));
        }

        if (transition != null && transition.getUserId() != null) {
            specs.add(filterByAdminId(transition.getUserId()));
        }

        return Specification.allOf(specs);
    }

    private static Specification<Area> fetchRelations() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                Fetch<Object, Object> regionFetch = root.fetch("region", JoinType.LEFT);
                Fetch<Object, Object> adminFetch = root.fetch("admin", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }

    private static Specification<Area> fullTextSearch(String keyword) {
        return (root, query, cb) -> {
            String lowerKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), lowerKeyword),
                    cb.like(cb.lower(root.get("code")), lowerKeyword)
            );
        };
    }

    private static Specification<Area> filterByAdminId(Long id) {
        return (root, query, cb) -> cb.equal(root.get("admin").get("id"), id);
    }
}
