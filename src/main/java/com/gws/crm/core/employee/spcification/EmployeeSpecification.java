package com.gws.crm.core.employee.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeCriteria;
import com.gws.crm.core.employee.entity.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filter(EmployeeCriteria criteria, Transition transition) {
        List<Specification<Employee>> specs = new ArrayList<>();
        specs.add(adminEmployee(transition.getUserId()));
        specs.add(filterByDeleted(criteria.isDeleted()));

        // New criteria implementations
        specs.add(filterByJobName(criteria.getJobName()));
        specs.add(filterByEnabled(criteria.getEnabled()));
        specs.add(fullTextSearch(criteria.getKeyword()));

        return Specification.allOf(specs);
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

    private static Specification<Employee> filterByJobName(String jobName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(jobName)) {
                return null;
            }
            return criteriaBuilder.equal(root.join("jobName").get("jobName"), jobName);
        };
    }

    private static Specification<Employee> filterByEnabled(String enabled) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(enabled)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("enabled"), enabled);
        };
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
                   // criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")), lowerKeyword)
            );
        };
    }
}
