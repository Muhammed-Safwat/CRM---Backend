package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.employee.dto.ActionResponse;
import com.gws.crm.core.employee.entity.ActionType;
import com.gws.crm.core.employee.entity.LeadActionDetails;
import com.gws.crm.core.employee.entity.UserAction;
import com.gws.crm.core.employee.mapper.ActionMapper;
import com.gws.crm.core.employee.repository.UserActionRepository;
import com.gws.crm.core.employee.service.LeadActionService;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.repository.GenericBaseLeadRepository;
import com.gws.crm.core.lookups.entity.CallOutcome;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Transactional
@Log
public abstract class GenericLeadActionServiceImp<T extends BaseLead> implements LeadActionService<T> {

    private final UserRepository userRepository;
    private final GenericBaseLeadRepository<T> leadRepository;
    private final UserActionRepository userActionRepository;
    private final ActionMapper actionMapper;

    protected GenericLeadActionServiceImp(
            UserRepository userRepository,
            GenericBaseLeadRepository<T> leadRepository,
            UserActionRepository userActionRepository,
            ActionMapper actionMapper
    ) {
        this.userRepository = userRepository;
        this.leadRepository = leadRepository;
        this.userActionRepository = userActionRepository;
        this.actionMapper = actionMapper;
    }


    @Override
    public ResponseEntity<?> getActions(long leadId, Transition transition) {
        List<UserAction> actions = userActionRepository.findActionsByLeadId(leadId);
        List<ActionResponse> responseList = actionMapper.toDto(actions);
        return success(responseList);
    }


    @Override
    @Transactional
    public void setLeadEditAction(T salesLead, Transition transition) {
        User editor = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        // === Create UserAction (EDIT) ===
        UserAction editAction = UserAction.builder()
                .creator(editor)
                .type(ActionType.EDIT)
                .description("Updated lead details")
                .createdAt(LocalDateTime.now())
                .build();

        // === Compose LeadActionDetails ===
        LeadActionDetails editDetails = LeadActionDetails.builder()
                .userAction(editAction)
                .lead(salesLead)
                .build();

        editAction.setLeadDetails(editDetails);

        // === Persist via cascade ===
        userActionRepository.save(editAction);

        // === Optional: update lastActionDate ===
        salesLead.setLastActionDate(LocalDateTime.now());
        leadRepository.save(salesLead);
    }



    protected String generateDescriptionBasedOnOutcome(String outcome) {
        StringBuilder description = new StringBuilder();
        description.append("Recorded an outcome: ").append(outcome);

        switch (outcome) {
            case "Call Back Later":
                description.append(". A follow-up call has been scheduled for later.");
                break;
            case "Meeting Scheduled":
                description.append(". A meeting has been scheduled with the lead.");
                break;
            case "Information Sent":
                description.append(". Relevant information has been sent to the lead.");
                break;
            case "Request for More Information":
                description.append(". The lead requested additional information.");
                break;
            case "Cancellation":
                description.append(". The lead has requested cancellation.");
                break;
            default:
                description.append(". Outcome recorded.");
                break;
        }
        return description.toString();
    }


    @Override
    @Transactional
    public void setLeadDeletionAction(T salesLead, Transition transition) {
        User deleter = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        // === Create the DELETE action ===
        UserAction deleteAction = UserAction.builder()
                .creator(deleter)
                .type(ActionType.DELETE)
                .description("Deleted the lead record")
                .createdAt(LocalDateTime.now())
                .build();

        // === Create the related LeadActionDetails ===
        LeadActionDetails deleteDetails = LeadActionDetails.builder()
                .userAction(deleteAction)
                .lead(salesLead)
                .comment("Lead was deleted by " + deleter.getName())
                .build();

        deleteAction.setLeadDetails(deleteDetails);

        // === Save action with cascade to details ===
        userActionRepository.save(deleteAction);

        // === Optional: update lastActionDate if lead is not being physically deleted ===
        salesLead.setLastActionDate(LocalDateTime.now());
        leadRepository.save(salesLead);
    }

    @Override
    @Transactional
    public void setLeadRestoreAction(T salesLead, Transition transition) {
        User restorer = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        // === Create the RESTORE action ===
        UserAction restoreAction = UserAction.builder()
                .creator(restorer)
                .type(ActionType.RESTORE)
                .description("Restored the lead record")
                .createdAt(LocalDateTime.now())
                .build();

        // === Create the related LeadActionDetails ===
        LeadActionDetails restoreDetails = LeadActionDetails.builder()
                .userAction(restoreAction)
                .lead(salesLead)
                .comment("Lead was restored by " + restorer.getName())
                .build();

        restoreAction.setLeadDetails(restoreDetails);

        // === Save the action and details ===
        userActionRepository.save(restoreAction);

        // === Optionally update the lead metadata ===
        salesLead.setLastActionDate(LocalDateTime.now());
        leadRepository.save(salesLead);
    }

}
