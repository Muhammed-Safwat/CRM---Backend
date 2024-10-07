package com.gws.crm.core.employee.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.EmployeeCriteria;
import com.gws.crm.core.employee.entity.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filter(EmployeeCriteria criteria, Transition transition) {
        List<Specification<Employee>> specs = new ArrayList<>();
        specs.add(adminEmployee(transition.getUserId()));
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


}
