package com.gws.crm.core.leads.spcification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.LeadCriteria;
import com.gws.crm.core.leads.entity.Lead;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LeadSpecification {

    public static Specification<Lead> filter(LeadCriteria leadCriteria, Transition transition) {
        List<Specification<Lead>> specs = new ArrayList<>();

        if (leadCriteria != null) {
            specs.add(fullTextSearch(leadCriteria.getKeyword()));
            specs.add(filterById(leadCriteria.getId()));
            specs.add(filterByStatus(leadCriteria.getStatusId()));
            specs.add(filterByInvestmentGoal(leadCriteria.getInvestmentGoalId()));
            specs.add(filterByCommunicateWay(leadCriteria.getCommunicateWayId()));
            specs.add(filterByCancelReasons(leadCriteria.getCancelReasonsId()));
            specs.add(filterBySalesRep(leadCriteria.getSalesRepId()));
            specs.add(filterByChannel(leadCriteria.getChannelId()));
            specs.add(filterByProject(leadCriteria.getProjectId()));
            specs.add(filterByDeleted(leadCriteria.isDeleted()));
            specs.add(filterByCreator(leadCriteria.getCreatorId()));
            specs.add(filterByAdminId(transition.getUserId()));
        }

        return Specification.allOf(specs);
    }

    private static Specification<Lead> fullTextSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")), lowerKeyword),
                    criteriaBuilder.like(root.join("phoneNumbers", JoinType.INNER).as(String.class), lowerKeyword)
            );
        };
    }

    private static Specification<Lead> filterById(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    private static Specification<Lead> filterByStatus(Long statusId) {
        return (root, query, criteriaBuilder) -> {
            if (statusId == null || statusId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status").get("id"), statusId);
        };
    }

    private static Specification<Lead> filterByInvestmentGoal(Long investmentGoalId) {
        return (root, query, criteriaBuilder) -> {
            if (investmentGoalId == null || investmentGoalId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("investmentGoal").get("id"), investmentGoalId);
        };
    }

    private static Specification<Lead> filterByCommunicateWay(Long communicateWayId) {
        return (root, query, criteriaBuilder) -> {
            if (communicateWayId == null || communicateWayId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("communicateWay").get("id"), communicateWayId);
        };
    }

    private static Specification<Lead> filterByCancelReasons(Long cancelReasonsId) {
        return (root, query, criteriaBuilder) -> {
            if (cancelReasonsId == null || cancelReasonsId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("cancelReasons").get("id"), cancelReasonsId);
        };
    }

    private static Specification<Lead> filterBySalesRep(Long salesRepId) {
        return (root, query, criteriaBuilder) -> {
            if (salesRepId == null || salesRepId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("salesRep").get("id"), salesRepId);
        };
    }

    private static Specification<Lead> filterByChannel(Long channelId) {
        return (root, query, criteriaBuilder) -> {
            if (channelId == null || channelId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("channel").get("id"), channelId);
        };
    }

    private static Specification<Lead> filterByProject(Long projectId) {
        return (root, query, criteriaBuilder) -> {
            if (projectId == null || projectId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("project").get("id"), projectId);
        };
    }

    private static Specification<Lead> filterByDeleted(boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    private static Specification<Lead> filterByCreator(Long creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("creator").get("id"), creatorId);
        };
    }

    private static Specification<Lead> filterByAdminId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id == 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("admin").get("id"), id);
        };
    }
}
