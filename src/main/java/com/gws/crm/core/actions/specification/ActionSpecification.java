package com.gws.crm.core.actions.specification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.dtos.ActionCriteria;
import com.gws.crm.core.actions.entity.UserAction;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ActionSpecification {

  public static Specification<UserAction> filter(ActionCriteria criteria, Transition transition) {
    List<Specification<UserAction>> specs = new ArrayList<>();

    log.info("Filtering actions with criteria: {}", criteria);

    specs.add(fetchAssociations());

    if (criteria != null) {
      specs.add(filterByCreatedAt(criteria.getCreatedAt()));
      specs.add(filterByCreatedAtRange(criteria.getCreatedAtFrom(), criteria.getCreatedAtTo()));
      specs.add(filterByActionDateRange(criteria.getActionDateFrom(), criteria.getActionDateTo()));

      specs.add(filterByType(criteria.getType()));
      specs.add(filterByTypes(criteria.getTypes()));

      specs.add(filterByCreatorId(criteria.getCreatorId()));
      specs.add(filterByCreatorIds(criteria.getCreatorIds()));
      specs.add(filterByCreatorName(criteria.getCreatorName()));

      specs.add(filterByLeadId(criteria.getLeadId()));
      specs.add(filterByLeadIds(criteria.getLeadIds()));
      specs.add(filterByLeadType(criteria.getLeadType()));

      specs.add(filterByKeyword(criteria.getKeyword()));
      specs.add(filterByDescription(criteria.getDescription()));
      specs.add(filterByComment(criteria.getComment()));

      specs.add(filterByHasComment(criteria.getHasComment()));
      specs.add(filterByHasCallBackTime(criteria.getHasCallBackTime()));
      specs.add(filterByHasNextActionDate(criteria.getHasNextActionDate()));

      specs.add(filterByCallOutcome(criteria.getCallOutcome()));
      specs.add(filterByCallOutcomes(criteria.getCallOutcomes()));

      specs.add(filterByStage(criteria.getStage()));
      specs.add(filterByStages(criteria.getStages()));

      specs.add(filterByCancellationReason(criteria.getCancellationReason()));
      specs.add(filterByCancellationReasons(criteria.getCancellationReasons()));
    }

    return Specification.allOf(specs);
  }

  private static Specification<UserAction> fetchAssociations() {
    return (root, query, cb) -> {
      if (query.getResultType() != Long.class && query.getResultType() != long.class) {
        root.fetch("creator", JoinType.LEFT);
        var leadDetailsFetch = root.fetch("leadDetails", JoinType.LEFT);
        leadDetailsFetch.fetch("lead", JoinType.LEFT);
        leadDetailsFetch.fetch("callOutcome", JoinType.LEFT);
      }
      return null;
    };
  }

  private static Specification<UserAction> filterByCreatedAt(List<LocalDateTime> createdAt) {
    return (root, query, cb) -> {
      if (createdAt == null || createdAt.isEmpty()) {
        return null;
      }
      return root.get("createdAt").in(createdAt);
    };
  }

  private static Specification<UserAction> filterByCreatedAtRange(LocalDateTime from, LocalDateTime to) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (from != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
      }
      if (to != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
      }

      return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private static Specification<UserAction> filterByActionDateRange(LocalDateTime from, LocalDateTime to) {
    return (root, query, cb) -> {
      if (from == null && to == null) {
        return null;
      }

      List<Predicate> predicates = new ArrayList<>();

      if (from != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
      }
      if (to != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
      }

      return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  private static Specification<UserAction> filterByType(String type) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(type)) {
        return null;
      }
      return cb.equal(root.get("type").get("displayValue"), type);
    };
  }

  private static Specification<UserAction> filterByTypes(List<String> types) {
    return (root, query, cb) -> {
      if (types == null || types.isEmpty()) {
        return null;
      }
      return root.get("type").get("displayValue").in(types);
    };
  }

  private static Specification<UserAction> filterByCreatorId(Long creatorId) {
    return (root, query, cb) -> {
      if (creatorId == null) {
        return null;
      }
      return cb.equal(root.get("creator").get("id"), creatorId);
    };
  }

  private static Specification<UserAction> filterByCreatorIds(List<Long> creatorIds) {
    return (root, query, cb) -> {
      if (creatorIds == null || creatorIds.isEmpty()) {
        return null;
      }
      return root.get("creator").get("id").in(creatorIds);
    };
  }

  private static Specification<UserAction> filterByCreatorName(String creatorName) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(creatorName)) {
        return null;
      }
      return cb.like(cb.lower(root.get("creator").get("name")),
          "%" + creatorName.toLowerCase() + "%");
    };
  }

  // Lead filters
  private static Specification<UserAction> filterByLeadId(Long leadId) {
    return (root, query, cb) -> {
      if (leadId == null) {
        return null;
      }
      return cb.equal(root.get("leadDetails").get("lead").get("id"), leadId);
    };
  }

  private static Specification<UserAction> filterByLeadIds(List<Long> leadIds) {
    return (root, query, cb) -> {
      if (leadIds == null || leadIds.isEmpty()) {
        return null;
      }
      return root.get("leadDetails").get("lead").get("id").in(leadIds);
    };
  }

  private static Specification<UserAction> filterByLeadType(String leadType) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(leadType)) {
        return null;
      }
      return cb.like(cb.lower(root.get("leadDetails").get("lead").get("class").get("simpleName")),
          "%" + leadType.toLowerCase() + "%");
    };
  }

  private static Specification<UserAction> filterByKeyword(String keyword) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(keyword)) {
        return null;
      }

      String searchTerm = "%" + keyword.toLowerCase() + "%";

      return cb.or(
          cb.like(cb.lower(root.get("description")), searchTerm),
          cb.like(cb.lower(root.get("leadDetails").get("comment")), searchTerm),
          cb.like(cb.lower(root.get("creator").get("name")), searchTerm));
    };
  }

  private static Specification<UserAction> filterByDescription(String description) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(description)) {
        return null;
      }
      return cb.like(cb.lower(root.get("description")),
          "%" + description.toLowerCase() + "%");
    };
  }

  private static Specification<UserAction> filterByComment(String comment) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(comment)) {
        return null;
      }
      return cb.like(cb.lower(root.get("leadDetails").get("comment")),
          "%" + comment.toLowerCase() + "%");
    };
  }

  private static Specification<UserAction> filterByHasComment(Boolean hasComment) {
    return (root, query, cb) -> {
      if (hasComment == null) {
        return null;
      }
      if (hasComment) {
        return cb.isNotNull(root.get("leadDetails").get("comment"));
      } else {
        return cb.isNull(root.get("leadDetails").get("comment"));
      }
    };
  }

  private static Specification<UserAction> filterByHasCallBackTime(Boolean hasCallBackTime) {
    return (root, query, cb) -> {
      if (hasCallBackTime == null) {
        return null;
      }
      if (hasCallBackTime) {
        return cb.isNotNull(root.get("leadDetails").get("callBackTime"));
      } else {
        return cb.isNull(root.get("leadDetails").get("callBackTime"));
      }
    };
  }

  private static Specification<UserAction> filterByHasNextActionDate(Boolean hasNextActionDate) {
    return (root, query, cb) -> {
      if (hasNextActionDate == null) {
        return null;
      }
      if (hasNextActionDate) {
        return cb.isNotNull(root.get("leadDetails").get("nextActionDate"));
      } else {
        return cb.isNull(root.get("leadDetails").get("nextActionDate"));
      }
    };
  }

  private static Specification<UserAction> filterByCallOutcome(String callOutcome) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(callOutcome)) {
        return null;
      }
      return cb.equal(root.get("leadDetails").get("callOutcome").get("name"), callOutcome);
    };
  }

  private static Specification<UserAction> filterByCallOutcomes(List<String> callOutcomes) {
    return (root, query, cb) -> {
      if (callOutcomes == null || callOutcomes.isEmpty()) {
        return null;
      }
      return root.get("leadDetails").get("callOutcome").get("name").in(callOutcomes);
    };
  }

  private static Specification<UserAction> filterByStage(String stage) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(stage)) {
        return null;
      }
      return cb.equal(root.get("leadDetails").get("stage"), stage);
    };
  }

  private static Specification<UserAction> filterByStages(List<String> stages) {
    return (root, query, cb) -> {
      if (stages == null || stages.isEmpty()) {
        return null;
      }
      return root.get("leadDetails").get("stage").in(stages);
    };
  }

  private static Specification<UserAction> filterByCancellationReason(String cancellationReason) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(cancellationReason)) {
        return null;
      }
      return cb.like(cb.lower(root.get("leadDetails").get("cancellationReason")),
          "%" + cancellationReason.toLowerCase() + "%");
    };
  }

  private static Specification<UserAction> filterByCancellationReasons(List<String> cancellationReasons) {
    return (root, query, cb) -> {
      if (cancellationReasons == null || cancellationReasons.isEmpty()) {
        return null;
      }
      return root.get("leadDetails").get("cancellationReason").in(cancellationReasons);
    };
  }
}
