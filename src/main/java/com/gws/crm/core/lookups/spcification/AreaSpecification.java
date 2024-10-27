package com.gws.crm.core.lookups.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.entity.Area;
import com.gws.crm.core.lookups.entity.BaseLookup;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AreaSpecification {

    public static Specification<Area> filter(String keyword, Transition transition) {
        List<Specification<Area>> specs = new ArrayList<>();
        specs.add(fullTextSearch(keyword));
        specs.add(filterByAdminId(transition.getUserId()));
        return Specification.allOf(specs);
    }

    // Full text search specification
    private static Specification<Area> fullTextSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction(); // Return a no-op if no keyword is present
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword)
            );
        };
    }

    private static Specification<Area> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, criteriaBuilder) -> {
            if (createdAt == null) {
                return null; // Return null if createdAt is null
            }
            return criteriaBuilder.equal(root.get("createdAt"), createdAt);
        };
    }

    private static Specification<Area> filterByAdminId(Long id) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("admin").get("id"), id);
        };
    }
}
