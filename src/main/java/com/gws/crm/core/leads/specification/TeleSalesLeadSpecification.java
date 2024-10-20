package com.gws.crm.core.leads.specification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.LeadCriteria;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeleSalesLeadSpecification {

    public static Specification<TeleSalesLead> filter(LeadCriteria leadCriteria, Transition transition) {
        List<Specification<TeleSalesLead>> specs = new ArrayList<>();

        if (leadCriteria != null) {
            specs.add(fullTextSearch(leadCriteria.getKeyword()));
            specs.add(filterByStatus(leadCriteria.getStatus()));
            specs.add(filterByInvestmentGoals(leadCriteria.getInvestmentGoal()));
            specs.add(filterByCommunicateWays(leadCriteria.getCommunicateWay()));
            specs.add(filterByCancelReasons(leadCriteria.getCancelReasons()));
            specs.add(filterByChannels(leadCriteria.getChannel()));
            specs.add(filterByBrokers(leadCriteria.getBroker()));
            specs.add(filterByProjects(leadCriteria.getProject()));
            specs.add(filterByCountry(leadCriteria.getCountry()));
            specs.add(filterByDeleted(leadCriteria.isDeleted()));
            specs.add(filterByCampaignId(leadCriteria.getCampaignId()));
            // specs.add(filterByLastActionDate(leadCriteria.getLastActionDate()));
            // specs.add(filterByLastActionNoAction(leadCriteria.getLastActionNoAction()));
            // specs.add(filterByStageDate(leadCriteria.getStageDate()));
            // specs.add(filterByActionDate(leadCriteria.getActionDate()));
            // specs.add(filterByAssignDate(leadCriteria.getAssignDate()));
            specs.add(filterByBudget(leadCriteria.getBudget()));
            // specs.add(filterByHasPayment(leadCriteria.getHasPayment()));
            // specs.add(filterByNoAnswers(leadCriteria.getNoAnswers()));
            specs.add(filterByCreatedAt(leadCriteria.getCreatedAt()));
            specs.add(filterByUser(leadCriteria, transition));
            specs.add(filterByCreator(leadCriteria.getCreator()));
        }

        return Specification.allOf(specs);
    }

    private static Specification<TeleSalesLead> filterByUser(LeadCriteria leadCriteria, Transition transition) {
        if (leadCriteria.isMyLead()) {
            return filterByCreator(transition.getUserId());
        } else if (transition.getRole().equals("ADMIN")) {
            return filterByAdminId(transition.getUserId());
        }
        return null;
    }

    // Full text search specification
    private static Specification<TeleSalesLead> fullTextSearch(String keyword) {
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


    private static Specification<TeleSalesLead> filterByStatus(Long statusId) {
        return (root, query, criteriaBuilder) -> {
            if (statusId == null || statusId <= 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status").get("id"), statusId);
        };
    }

    private static Specification<TeleSalesLead> filterByInvestmentGoals(List<Long> investmentGoals) {
        return (root, query, criteriaBuilder) -> {
            if (investmentGoals == null || investmentGoals.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("investmentGoal", JoinType.INNER).get("id").in(investmentGoals);
        };
    }

    private static Specification<TeleSalesLead> filterByCommunicateWays(List<Long> communicateWays) {
        return (root, query, criteriaBuilder) -> {
            if (communicateWays == null || communicateWays.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("communicateWay", JoinType.INNER).get("id").in(communicateWays);
        };
    }

    private static Specification<TeleSalesLead> filterByCancelReasons(List<Long> cancelReasons) {
        return (root, query, criteriaBuilder) -> {
            if (cancelReasons == null || cancelReasons.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("cancelReasons", JoinType.INNER).get("id").in(cancelReasons);
        };
    }

    private static Specification<TeleSalesLead> filterBySalesReps(List<Long> salesReps) {
        return (root, query, criteriaBuilder) -> {
            if (salesReps == null || salesReps.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("salesRep", JoinType.INNER).get("id").in(salesReps);
        };
    }

    private static Specification<TeleSalesLead> filterByChannels(List<Long> channels) {
        return (root, query, criteriaBuilder) -> {
            if (channels == null || channels.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("channel", JoinType.INNER).get("id").in(channels);
        };
    }

    private static Specification<TeleSalesLead> filterByBrokers(List<Long> brokers) {
        return (root, query, criteriaBuilder) -> {
            if (brokers == null || brokers.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("broker", JoinType.INNER).get("id").in(brokers);
        };
    }

    private static Specification<TeleSalesLead> filterByProjects(List<Long> projects) {
        return (root, query, criteriaBuilder) -> {
            if (projects == null || projects.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("project", JoinType.INNER).get("id").in(projects);
        };
    }

    private static Specification<TeleSalesLead> filterByCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(country)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("country"), country);
        };
    }

    private static Specification<TeleSalesLead> filterByCreator(Long creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("creator").get("id"), creatorId);
        };
    }

    private static Specification<TeleSalesLead> filterByDeleted(boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    private static Specification<TeleSalesLead> filterByCampaignId(String campaignId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(campaignId)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("campaignId"), campaignId);
        };
    }

    /*
      private static Specification<TeleSalesLead> filterByLastActionDate(LocalDate lastActionDate) {
          return (root, query, criteriaBuilder) -> {
              if (lastActionDate == null) {
                  return null;
              }
              return criteriaBuilder.equal(root.get("lastActionDate"), lastActionDate);
          };
      }

      private static Specification<TeleSalesLead> filterByLastActionNoAction(LocalDate lastActionNoAction) {
          return (root, query, criteriaBuilder) -> {
              if (lastActionNoAction == null) {
                  return null;
              }
              return criteriaBuilder.equal(root.get("lastActionNoAction"), lastActionNoAction);
          };
      }


          private static Specification<TeleSalesLead> filterByStageDate(LocalDate stageDate) {
              return (root, query, criteriaBuilder) -> {
                  if (stageDate == null) {
                      return null;
                  }
                  return criteriaBuilder.equal(root.get("stageDate"), stageDate);
              };
          }

          private static Specification<TeleSalesLead> filterByActionDate(LocalDate actionDate) {
              return (root, query, criteriaBuilder) -> {
                  if (actionDate == null) {
                      return null;
                  }
                  return criteriaBuilder.equal(root.get("actionDate"), actionDate);
              };
          }

          private static Specification<TeleSalesLead> filterByAssignDate(LocalDate assignDate) {
              return (root, query, criteriaBuilder) -> {
                  if (assignDate == null) {
                      return null;
                  }
                  return criteriaBuilder.equal(root.get("assignDate"), assignDate);
              };
          }
      */
    private static Specification<TeleSalesLead> filterByBudget(String budget) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(budget)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("budget"), budget);
        };
    }

    /*
        private static Specification<TeleSalesLead> filterByHasPayment(String hasPayment) {
            return (root, query, criteriaBuilder) -> {
                if (!StringUtils.hasText(hasPayment)) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("hasPayment"), hasPayment);
            };
        }

        private static Specification<TeleSalesLead> filterByNoAnswers(String noAnswers) {
            return (root, query, criteriaBuilder) -> {
                if (!StringUtils.hasText(noAnswers)) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("noAnswers"), noAnswers);
            };
        }
    */
    private static Specification<TeleSalesLead> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, criteriaBuilder) -> {
            if (createdAt == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("createdAt"), createdAt);
        };
    }

    private static Specification<TeleSalesLead> filterByAdminId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id == 0) {
                return null;
            }
            return criteriaBuilder.equal(root.get("admin").get("id"), id);
        };
    }
}
