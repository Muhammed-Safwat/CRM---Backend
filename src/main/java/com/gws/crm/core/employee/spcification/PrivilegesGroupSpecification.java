package com.gws.crm.core.employee.spcification;

import com.gws.crm.authentication.dto.PrivilegeGroupCriteria;
import com.gws.crm.authentication.entity.PrivilegeGroup;
import com.gws.crm.common.entities.Transition;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PrivilegesGroupSpecification {

    public static Specification<PrivilegeGroup> filter(PrivilegeGroupCriteria criteria, Transition transition) {
        List<Specification<PrivilegeGroup>> specs = new ArrayList<>();

        specs.add(filterByStatus(criteria.getStatus()));

        specs.add(fullTextSearch(criteria.getKeyword()));

        return Specification.allOf(specs);
    }

    private static Specification<PrivilegeGroup> filterByStatus(Boolean status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    private static Specification<PrivilegeGroup> fullTextSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobName")), lowerKeyword)
            );
        };
    }
}
