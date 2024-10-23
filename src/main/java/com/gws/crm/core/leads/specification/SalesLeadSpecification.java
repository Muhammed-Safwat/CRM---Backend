package com.gws.crm.core.leads.specification;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.dto.SalesLeadCriteria;
import com.gws.crm.core.leads.entity.SalesLead;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesLeadSpecification<T extends SalesLead> {

    public static <T extends SalesLead> Specification<T> filter(SalesLeadCriteria salesLeadCriteria, Transition transition) {
        List<Specification<T>> specs = new ArrayList<>();

        if (salesLeadCriteria != null) {
            specs.add(fullTextSearch(salesLeadCriteria.getKeyword()));
            specs.add(filterByStatus(salesLeadCriteria.getStatus()));
            specs.add(filterByInvestmentGoals(salesLeadCriteria.getInvestmentGoal()));
            specs.add(filterByCommunicateWays(salesLeadCriteria.getCommunicateWay()));
            specs.add(filterByCancelReasons(salesLeadCriteria.getCancelReasons()));
            specs.add(filterByChannels(salesLeadCriteria.getChannel()));
            specs.add(filterByBrokers(salesLeadCriteria.getBroker()));
            specs.add(filterByProjects(salesLeadCriteria.getProject()));
            specs.add(filterByCountry(salesLeadCriteria.getCountry()));
            specs.add(filterByDeleted(salesLeadCriteria.isDeleted()));
            specs.add(filterByCampaignId(salesLeadCriteria.getCampaignId()));
            // specs.add(filterByLastActionDate(leadCriteria.getLastActionDate()));
            // specs.add(filterByLastActionNoAction(leadCriteria.getLastActionNoAction()));
            // specs.add(filterByStageDate(leadCriteria.getStageDate()));
            // specs.add(filterByActionDate(leadCriteria.getActionDate()));
            // specs.add(filterByAssignDate(leadCriteria.getAssignDate()));
            specs.add(filterByBudget(salesLeadCriteria.getBudget()));
            // specs.add(filterByHasPayment(leadCriteria.getHasPayment()));
            // specs.add(filterByNoAnswers(leadCriteria.getNoAnswers()));
            specs.add(filterByCreatedAt(salesLeadCriteria.getCreatedAt()));
            specs.add(filterByUser(salesLeadCriteria, transition));
            specs.add(filterByCreator(salesLeadCriteria.getCreator()));
        }

        return Specification.allOf(specs);
    }

    private static <T extends SalesLead> Specification<T> filterByUser(SalesLeadCriteria leadCriteria, Transition transition) {
        if (leadCriteria.isMyLead()) {
            return filterByCreator(transition.getUserId());
        } else if (transition.getRole().equals("ADMIN")) {
            return filterByAdminId(transition.getUserId());
        }
        return null;
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

    private static <T extends SalesLead> Specification<T> filterBySalesReps(List<Long> salesReps) {
        return (root, query, criteriaBuilder) -> {
            if (salesReps == null || salesReps.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return root.join("salesRep", JoinType.INNER).get("id").in(salesReps);
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

    private static <T extends SalesLead> Specification<T> filterByCreator(Long creatorId) {
        return (root, query, criteriaBuilder) -> {
            if (creatorId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("creator").get("id"), creatorId);
        };
    }

    private static <T extends SalesLead> Specification<T> filterByDeleted(boolean deleted) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), deleted);
    }

    private static <T extends SalesLead> Specification<T> filterByCampaignId(String campaignId) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(campaignId)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("campaignId"), campaignId);
        };
    }

    /*
      private static <T extends SalesLead>  Specification<SalesLead> filterByLastActionDate(LocalDate lastActionDate) {
          return (root, query, criteriaBuilder) -> {
              if (lastActionDate == null) {
                  return null;
              }
              return criteriaBuilder.equal(root.get("lastActionDate"), lastActionDate);
          };
      }

      private static <T extends SalesLead>  Specification<SalesLead> filterByLastActionNoAction(LocalDate lastActionNoAction) {
          return (root, query, criteriaBuilder) -> {
              if (lastActionNoAction == null) {
                  return null;
              }
              return criteriaBuilder.equal(root.get("lastActionNoAction"), lastActionNoAction);
          };
      }


          private static <T extends SalesLead>  Specification<SalesLead> filterByStageDate(LocalDate stageDate) {
              return (root, query, criteriaBuilder) -> {
                  if (stageDate == null) {
                      return null;
                  }
                  return criteriaBuilder.equal(root.get("stageDate"), stageDate);
              };
          }

          private static <T extends SalesLead>  Specification<SalesLead> filterByActionDate(LocalDate actionDate) {
              return (root, query, criteriaBuilder) -> {
                  if (actionDate == null) {
                      return null;
                  }
                  return criteriaBuilder.equal(root.get("actionDate"), actionDate);
              };
          }

          private static <T extends SalesLead>  Specification<SalesLead> filterByAssignDate(LocalDate assignDate) {
              return (root, query, criteriaBuilder) -> {
                  if (assignDate == null) {
                      return null;
                  }
                  return criteriaBuilder.equal(root.get("assignDate"), assignDate);
              };
          }
      */
    private static <T extends SalesLead> Specification<T> filterByBudget(String budget) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(budget)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("budget"), budget);
        };
    }

    /*
        private static <T extends SalesLead>  Specification<SalesLead> filterByHasPayment(String hasPayment) {
            return (root, query, criteriaBuilder) -> {
                if (!StringUtils.hasText(hasPayment)) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("hasPayment"), hasPayment);
            };
        }

        private static <T extends SalesLead>  Specification<SalesLead> filterByNoAnswers(String noAnswers) {
            return (root, query, criteriaBuilder) -> {
                if (!StringUtils.hasText(noAnswers)) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("noAnswers"), noAnswers);
            };
        }
    */
    private static <T extends SalesLead> Specification<T> filterByCreatedAt(LocalDate createdAt) {
        return (root, query, criteriaBuilder) -> {
            if (createdAt == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("createdAt"), createdAt);
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
