package com.gws.crm.core.lookups.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.entity.BaseLookup;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LookupSpecification<T extends BaseLookup> {

    public static <T extends BaseLookup> Specification<T> filter(String keyword,long userId, Transition transition) {
        List<Specification<T>> specs = new ArrayList<>();
        specs.add(fullTextSearch(keyword));
        specs.add(filterByAdminId(userId));
        return Specification.allOf(specs);
    }

    // Full text search specification
    private static <T extends BaseLookup> Specification<T> fullTextSearch(String keyword) {
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

    private static <T extends BaseLookup> Specification<T> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, criteriaBuilder) -> {
            if (createdAt == null) {
                return null; // Return null if createdAt is null
            }
            return criteriaBuilder.equal(root.get("createdAt"), createdAt);
        };
    }

    private static <T extends BaseLookup> Specification<T> filterByAdminId(long id) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("admin").get("id"), id);
        };
    }
}
