package com.gws.crm.core.actions.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.dtos.ActionCriteria;
import com.gws.crm.core.actions.service.DashboardActionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dashboard/actions")
@AllArgsConstructor
public class DashboardActionController {

    private final DashboardActionService dashboardActionService;

    @PostMapping("search")
    public ResponseEntity<?> searchActions(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("by-date-range")
    public ResponseEntity<?> getActionsByDateRange(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("by-type")
    public ResponseEntity<?> getActionsByType(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("by-creator")
    public ResponseEntity<?> getActionsByCreator(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("recent")
    public ResponseEntity<?> getRecentActions(@RequestBody ActionCriteria criteria, Transition transition) {

        if (criteria.getCreatedAtFrom() == null && criteria.getCreatedAtTo() == null) {
            int days = 7;
            criteria.setCreatedAtFrom(java.time.LocalDateTime.now().minusDays(days));
            criteria.setCreatedAtTo(java.time.LocalDateTime.now());
        }

        if (criteria.getSortBy() == null) {
            criteria.setSortBy("createdAt");
        }
        if (criteria.getSortDirection() == null) {
            criteria.setSortDirection("DESC");
        }

        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("by-lead")
    public ResponseEntity<?> getActionsByLead(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("with-comments")
    public ResponseEntity<?> getActionsWithComments(@RequestBody ActionCriteria criteria, Transition transition) {
        criteria.setHasComment(true);

        if (criteria.getSortBy() == null) {
            criteria.setSortBy("createdAt");
        }
        if (criteria.getSortDirection() == null) {
            criteria.setSortDirection("DESC");
        }

        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("call-outcomes")
    public ResponseEntity<?> getActionsByCallOutcome(@RequestBody ActionCriteria criteria, Transition transition) {

        if (criteria.getSortBy() == null) {
            criteria.setSortBy("createdAt");
        }
        if (criteria.getSortDirection() == null) {
            criteria.setSortDirection("DESC");
        }

        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("by-stage")
    public ResponseEntity<?> getActionsByStage(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("by-cancellation-reason")
    public ResponseEntity<?> getActionsByCancellationReason(@RequestBody ActionCriteria criteria,
            Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("with-callback-time")
    public ResponseEntity<?> getActionsWithCallbackTime(@RequestBody ActionCriteria criteria, Transition transition) {

        criteria.setHasCallBackTime(true);

        if (criteria.getSortBy() == null) {
            criteria.setSortBy("createdAt");
        }
        if (criteria.getSortDirection() == null) {
            criteria.setSortDirection("DESC");
        }

        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("with-next-action-date")
    public ResponseEntity<?> getActionsWithNextActionDate(@RequestBody ActionCriteria criteria, Transition transition) {
        criteria.setHasNextActionDate(true);
 
        if (criteria.getSortBy() == null) {
            criteria.setSortBy("createdAt");
        }
        if (criteria.getSortDirection() == null) {
            criteria.setSortDirection("DESC");
        }

        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("by-lead-type")
    public ResponseEntity<?> getActionsByLeadType(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

    @PostMapping("advanced-search")
    public ResponseEntity<?> advancedSearch(@RequestBody ActionCriteria criteria, Transition transition) {
        return dashboardActionService.getActions(criteria, transition);
    }

}
