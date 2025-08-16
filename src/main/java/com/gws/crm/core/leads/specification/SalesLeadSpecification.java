package com.gws.crm.core.leads.specification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.SalesLeadCriteria;
import com.gws.crm.core.leads.entity.SalesLead;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SalesLeadSpecification<T extends SalesLead> {

    public static <T extends SalesLead> Specification<T> filter(SalesLeadCriteria salesLeadCriteria, Transition transition) {
        List<Specification<T>> specs = new ArrayList<>();
        log.info(salesLeadCriteria.toString());
        List<Long> ids = new ArrayList<>();
        if (salesLeadCriteria.getSubordinates() != null) {
            ids.addAll(salesLeadCriteria.getSubordinates());
        }
        ids.add(transition.getUserId());
        if (salesLeadCriteria != null) {
            specs.add(getOnlyForAdmin(transition));
            specs.add(orderByCreatedOrUpdated());
            specs.add(fullTextSearch(salesLeadCriteria.getKeyword()));
            specs.add(filterByStatus(salesLeadCriteria.getStatus()));
            specs.add(filterByInvestmentGoals(salesLeadCriteria.getInvestmentGoal()));
            specs.add(filterByCommunicateWays(salesLeadCriteria.getCommunicateWay()));
            specs.add(filterByCancelReasons(salesLeadCriteria.getCancelReasons()));
            specs.add(filterByChannels(salesLeadCriteria.getChannel()));
            specs.add(filterByArchived(salesLeadCriteria.getArchived()));
            specs.add(filterByDeleted(salesLeadCriteria.getDeleted()));
            specs.add(filterByDelayed(salesLeadCriteria.getDelayed()));
            specs.add(filterByBrokers(salesLeadCriteria.getBroker()));
            specs.add(filterByProjects(salesLeadCriteria.getProject()));
            specs.add(filterByCountry(salesLeadCriteria.getCountry()));
            specs.add(filterByUser(salesLeadCriteria, transition));
            specs.add(filterByCampaignId(salesLeadCriteria.getCampaignId()));
            specs.add(filterByBudget(salesLeadCriteria.getBudget()));
            specs.add(filterByCreatedAt(salesLeadCriteria.getCreatedAt()));
            specs.add(filterByStageDate(salesLeadCriteria.getStageDate()));
            specs.add(filterByActionDate(salesLeadCriteria.getActionDate()));
            specs.add(filterByAssignDate(salesLeadCriteria.getAssignDate()));
            specs.add(filterByStage(salesLeadCriteria.getStage()));
            specs.add(filterByUser(ids, salesLeadCriteria.getMyLead(), transition));
            specs.add(filterBySalesReps(salesLeadCriteria.getSalesRep(), transition));
            specs.add(filterByCreators(salesLeadCriteria.getCreator(), transition));
            specs.add(filterByLastActionDate(salesLeadCriteria.getLastActionDate()));
            specs.add(filterByNextActionDate(salesLeadCriteria.getNextActionDate()));
        }

        return Specification.allOf(specs);
    }

    public static <T extends SalesLead> Specification<T> getOnlyForAdmin(Transition transition){
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (transition.getRole().equals("USER")) {

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("salesRep").get("id"), transition.getUserId())
                );
            } else if (transition.getRole().equals("ADMIN")) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("admin").get("id"), transition.getUserId())
                );
            }
            return predicate;
        };
    }

    public static <T extends SalesLead> Specification<T> orderByCreatedOrUpdated() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(
                    criteriaBuilder.desc(root.get("createdAt")),
                    criteriaBuilder.desc(root.get("updatedAt"))
            );
            return criteriaBuilder.conjunction();
        };
    }

    private static <T extends SalesLead> Specification<T> filterByUser(SalesLeadCriteria leadCriteria, Transition transition) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (transition.getRole().equals("USER")) {

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("salesRep").get("id"), transition.getUserId())
                );
            } else if (leadCriteria.getMyLead() != null && leadCriteria.getMyLead() && transition.getRole().equals(
                    "ADMIN")) {

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.isNull(root.get("salesRep"))
                );
            }
            return predicate;
        };
    }

    private static <T extends SalesLead> Specification<T> filterByUser(List<Long> ids, Boolean isMyLead,
                                                                       Transition transition) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (isMyLead == null && transition.getRole().equals("USER")) {
                predicate = criteriaBuilder.and(predicate, root.join("salesRep", JoinType.INNER).get("id").in(ids));
            } else if (isMyLead != null && isMyLead && transition.getRole().equals("ADMIN")) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.isNull(root.get("salesRep"))
                );
            } else if (isMyLead != null && isMyLead && transition.getRole().equals("USER")) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("salesRep").get("id"), transition.getUserId())
                );
            }
            return predicate;
        };
    }


    // Full text search specification
    private static <T extends SalesLead> Specification<T> fullTextSearch(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction();
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("phoneNumbers").get("phone")), lowerKeyword)
            );
        };
    }


    private static <T extends SalesLead> Specification<T> filterByStatus(Long statusId) {
        return (root, query, criteriaBuilder) -> {
            if (statusId == null || statusId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status").get("id"), statusId);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByInvestmentGoals(List<Long> investmentGoals) {
        return (root, query, criteriaBuilder) -> {
            if (investmentGoals == null || investmentGoals.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("investmentGoal", JoinType.INNER).get("id").in(investmentGoals);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByCommunicateWays(List<Long> communicateWays) {
        return (root, query, criteriaBuilder) -> {
            if (communicateWays == null || communicateWays.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("communicateWay", JoinType.INNER).get("id").in(communicateWays);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByCancelReasons(List<Long> cancelReasons) {
        return (root, query, criteriaBuilder) -> {
            if (cancelReasons == null || cancelReasons.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("cancelReasons", JoinType.INNER).get("id").in(cancelReasons);
        };
    }

    private static <T extends SalesLead> Specification<T> filterBySalesReps(List<Long> salesReps, Transition transition) {
        return (root, query, criteriaBuilder) -> {
            if (salesReps == null || salesReps.isEmpty()) {
                return null;
            }

            return root.join("salesRep", JoinType.INNER).get("id").in(salesReps);
        };
    }

    private static <T extends SalesLead> Specification<T> filterBySubSalesReps(List<Long> salesReps,
                                                                               Transition transition) {
        return (root, query, criteriaBuilder) -> {
            if (salesReps == null || salesReps.isEmpty()) {
                return null;
            }

            return root.join("salesRep", JoinType.INNER).get("id").in(salesReps);
        };
    }


    private static <T extends SalesLead> Specification<T> filterBySub(List<Long> salesReps, boolean isMyLeads) {
        return (root, query, criteriaBuilder) -> {
            if (salesReps == null || salesReps.isEmpty() || isMyLeads) {
                return null;
            }

            return root.join("salesRep", JoinType.INNER).get("id").in(salesReps);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByStage(List<Long> stage) {
        return (root, query, criteriaBuilder) -> {
            if (stage == null || stage.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("stage", JoinType.INNER).get("id").in(stage);
        };
    }


    private static <T extends SalesLead> Specification<T> filterByCreators(List<Long> creatorId, Transition transition) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return null;
            }
            return root.join("creator", JoinType.INNER).get("id").in(creatorId);
        };
    }

    private static <T extends SalesLead> Specification<T> filterBySubCreators(List<Long> creatorId,
                                                                              Transition transition) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null || creatorId.isEmpty()) {
                return null;
            }
            return root.join("creator", JoinType.INNER).get("id").in(creatorId);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByChannels(List<Long> channels) {
        return (root, query, criteriaBuilder) -> {
            if (channels == null || channels.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("channel", JoinType.INNER).get("id").in(channels);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByBrokers(List<Long> brokers) {
        return (root, query, criteriaBuilder) -> {
            if (brokers == null || brokers.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("broker", JoinType.INNER).get("id").in(brokers);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByProjects(List<Long> projects) {
        return (root, query, criteriaBuilder) -> {
            if (projects == null || projects.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("project", JoinType.INNER).get("id").in(projects);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(country)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("country"), country);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByDelayed(Boolean delayed) {
        return (root, query, criteriaBuilder) -> {
            if (delayed == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("delay"), delayed);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByArchived(Boolean archived) {
        return (root, query, criteriaBuilder) -> {
            if (archived == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("archive"), archived);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) -> {
            if (deleted == null) {
                return null;
            }

            return criteriaBuilder.equal(root.get("deleted"), deleted);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByCampaignId(String campaignId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(campaignId)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("campaignId"), campaignId);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByBudget(String budget) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(budget)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("budget"), budget);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByCreatedAt(List<LocalDateTime> createdAt) {
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

    private static <T extends SalesLead> Specification<T> filterByStageDate(List<LocalDateTime> stageDate) {
        return (root, query, criteriaBuilder) -> {
            if (stageDate == null || stageDate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startDate = stageDate.size() > 0 ? stageDate.get(0) : null;
            LocalDateTime endDate = stageDate.size() > 1 ? stageDate.get(1) : null;

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("stageDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("stageDate"), LocalDateTime.now());
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("stageDate"), endDate);
            }

            return criteriaBuilder.conjunction();
        };
    }

    private static <T extends SalesLead> Specification<T> filterByActionDate(List<LocalDateTime> actionDate) {
        return (root, query, criteriaBuilder) -> {
            if (actionDate == null || actionDate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startDate = actionDate.size() > 0 ? actionDate.get(0) : null;
            LocalDateTime endDate = actionDate.size() > 1 ? actionDate.get(1) : null;

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("actionDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("actionDate"), LocalDateTime.now());
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("actionDate"), endDate);
            }

            return criteriaBuilder.conjunction();
        };
    }

    private static <T extends SalesLead> Specification<T> filterByAssignDate(List<LocalDateTime> assignDate) {
        return (root, query, criteriaBuilder) -> {
            if (assignDate == null || assignDate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startDate = assignDate.size() > 0 ? assignDate.get(0) : null;
            LocalDateTime endDate = assignDate.size() > 1 ? assignDate.get(1) : null;

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("assignDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("assignDate"), LocalDateTime.now());
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("assignDate"), endDate);
            }

            return criteriaBuilder.conjunction();
        };
    }

    private static <T extends SalesLead> Specification<T> filterByLastActionDate(List<LocalDateTime> lastActionDate) {
        return (root, query, criteriaBuilder) -> {
            if (lastActionDate == null || lastActionDate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startDate = lastActionDate.size() > 0 ? lastActionDate.get(0) : null;
            LocalDateTime endDate = lastActionDate.size() > 1 ? lastActionDate.get(1) : null;

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("lastActionDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("lastActionDate"), LocalDateTime.now());
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("lastActionDate"), endDate);
            }

            return criteriaBuilder.conjunction();
        };
    }

    private static <T extends SalesLead> Specification<T> filterByNextActionDate(List<LocalDateTime> nextActionDate) {
        return (root, query, criteriaBuilder) -> {
            if (nextActionDate == null || nextActionDate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startDate = nextActionDate.size() > 0 ? nextActionDate.get(0) : null;
            LocalDateTime endDate = nextActionDate.size() > 1 ? nextActionDate.get(1) : null;

            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("nextActionDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("nextActionDate"), LocalDateTime.now());
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("nextActionDate"), endDate);
            }

            return criteriaBuilder.conjunction();
        };
    }

    private static <T extends SalesLead> Specification<T> filterByAdminId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id == 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("admin").get("id"), id);
        };
    }
}
