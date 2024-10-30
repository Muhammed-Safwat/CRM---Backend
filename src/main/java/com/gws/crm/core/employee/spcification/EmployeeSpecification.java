package com.gws.crm.core.employee.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeCriteria;
import com.gws.crm.core.employee.entity.Employee;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filter(EmployeeCriteria criteria, Transition transition) {
        List<Specification<Employee>> specs = new ArrayList<>();
        specs.add(adminEmployee(transition.getUserId()));
        specs.add(filterByDeleted(criteria.isDeleted()));

        // New criteria implementations
        specs.add(filterByJobName(criteria.getJobName()));
        specs.add(filterByEnabled(criteria.getStatus()));
        specs.add(filterByCreatedAt(criteria.getCreatedAt()));
        specs.add(fullTextSearch(criteria.getKeyword()));

        return Specification.allOf(specs);
    }

    private static Specification<Employee> filterByCreatedAt(List<LocalDateTime> createdAt){
        return (root, query, criteriaBuilder) -> {
            if (createdAt == null || createdAt.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startDate = createdAt.size() > 0 ? createdAt.get(0) : null;
            LocalDateTime endDate = createdAt.size() > 1 ? createdAt.get(1) : null;

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), LocalDateTime.now());
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate);
            }

            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<Employee> adminEmployee(Long adminId) {
        return (root, query, criteriaBuilder) -> {
            if (adminId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.join("admin").get("id"), adminId);
        };
    }

    private static Specification<Employee> filterByDeleted(boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    private static Specification<Employee> filterByJobName(List<Long> jobName) {
        return (root, query, criteriaBuilder) -> {
            if (jobName == null ||  jobName.isEmpty()) {
                return null;
            }
            return root.join("jobName", JoinType.INNER).get("id").in(jobName);
        };
    }

    private static Specification<Employee> filterByEnabled(String status) {
        if(!StringUtils.hasText(status)) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
       if(StringUtils.hasText(status) && status.equalsIgnoreCase("Active")) {
           return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true);
       } else {
           return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), false);
       }
    }

    private static Specification<Employee> fullTextSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction();
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), lowerKeyword)
            );
        };
    }
}
