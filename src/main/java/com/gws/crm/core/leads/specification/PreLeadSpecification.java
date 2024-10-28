package com.gws.crm.core.leads.specification;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.PreLeadCriteria;
import com.gws.crm.core.leads.entity.PreLead;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PreLeadSpecification {

    public static Specification<PreLead> filter(PreLeadCriteria leadCriteria, Transition transition) {
        List<Specification<PreLead>> specs = new ArrayList<>();

        if (leadCriteria != null) {
            specs.add(fullTextSearch(leadCriteria.getKeyword()));
            specs.add(filterByCampaignId(leadCriteria.getCampaignId()));
            specs.add(filterByDeleted(leadCriteria.isDeleted()));
            specs.add(filterByImported(leadCriteria.isImported()));
            specs.add(filterByCreatedAt(leadCriteria.getCreatedAt()));
            specs.add(filterByUser(leadCriteria, transition));
            specs.add(filterByCreator(leadCriteria.getCreator()));
            specs.add(filterByCountry(leadCriteria.getCountry()));
        }

        return Specification.allOf(specs);
    }

    private static Specification<PreLead> filterByUser(PreLeadCriteria leadCriteria, Transition transition) {
        if (leadCriteria.isMyLead()) {
            return filterByCreator(List.of(transition.getUserId()));
        } else if (transition.getRole().equals("ADMIN")) {
            return filterByAdminId(transition.getUserId());
        }
        return null;
    }

    // Full text search specification
    private static Specification<PreLead> fullTextSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction();
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("phoneNumbers").get("phone")), lowerKeyword)
            );
        };
    }

    private static Specification<PreLead> filterByCountry(String country) {
        return ((root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(country)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("country"), country);
        });
    }

    private static Specification<PreLead> filterByCreator(List<Long> creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("creator", JoinType.INNER).get("id").in(creatorId);
        };
    }

    private static Specification<PreLead> filterByDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    private static Specification<PreLead> filterByImported(Boolean imported) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("imported"), imported);
    }

    private static Specification<PreLead> filterByCampaignId(List<Integer> campaignId) {
        return (root, query, criteriaBuilder) -> {
            if (campaignId == null || campaignId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("campaign", JoinType.INNER).get("id").in(campaignId);
        };
    }


    private static Specification<PreLead> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, criteriaBuilder) -> {
            if (createdAt == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("createdAt"), createdAt);
        };
    }

    private static Specification<PreLead> filterByAdminId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id == 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("admin").get("id"), id);
        };
    }
}
