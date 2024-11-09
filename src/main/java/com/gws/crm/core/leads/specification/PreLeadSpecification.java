package com.gws.crm.core.leads.specification;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.PreLeadCriteria;
import com.gws.crm.core.leads.entity.PreLead;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PreLeadSpecification {

    public static Specification<PreLead> filter(PreLeadCriteria leadCriteria, Transition transition) {
        List<Specification<PreLead>> specs = new ArrayList<>();
        List<Long > ids = new ArrayList<>();
        if(leadCriteria.getSubordinates() != null){
            ids.addAll(leadCriteria.getSubordinates());
        }
        ids.add(transition.getUserId());
        if (leadCriteria != null) {
            specs.add(fullTextSearch(leadCriteria.getKeyword()));
            specs.add(filterByCampaignId(leadCriteria.getCampaignId()));
            specs.add(filterByDeleted(leadCriteria.isDeleted()));
            specs.add(filterByImported(leadCriteria.isImported()));
            specs.add(filterByCreatedAt(leadCriteria.getCreatedAt()));
            specs.add(filterByUser(ids,leadCriteria.isMyLead(), transition));
            specs.add(filterByCreators(leadCriteria.getCreator(), transition));
            specs.add(filterByCountry(leadCriteria.getCountry()));

        }

        return Specification.allOf(specs);
    }

    private static Specification<PreLead> filterByUser(List<Long> ids,boolean isMyLead, Transition transition) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (!isMyLead  && transition.getRole().equals("USER")) {

                predicate = criteriaBuilder.and(predicate,
                        root.join("creator",JoinType.INNER).get("id").in(ids)
                );
            } else if (isMyLead  && transition.getRole().equals("USER")) {

                predicate = criteriaBuilder.and(predicate,
                        root.join("creator",JoinType.INNER).get("id").in(transition.getUserId())
                );
            } else if (isMyLead && transition.getRole().equals("ADMIN")) {

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("creator").get("id"), transition.getUserId())
                );
            }else if (!isMyLead  && transition.getRole().equals("ADMIN")) {

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("admin").get("id"), transition.getUserId())
                );
            }
            return predicate;
        };
    }

    private static Specification<PreLead> filterByCreators(List<Long> creatorId, Transition transition) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty() ) {
                return null;
            }
            return root.join("creator", JoinType.INNER).get("id").in(creatorId);
        };
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
