package com.gws.crm.core.actions.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.actions.dtos.ActionResponse;
import com.gws.crm.core.actions.entity.ActionType;
import com.gws.crm.core.actions.entity.LeadActionDetails;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.actions.event.*;
import com.gws.crm.core.actions.mapper.ActionMapper;
import com.gws.crm.core.actions.repository.UserActionRepository;
import com.gws.crm.core.actions.service.LeadActionService;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.repository.GenericBaseLeadRepository;
import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public ResponseEntity<?> getActions(long leadId, int page, int size, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserAction> actions = userActionRepository.findActionsByLeadId(leadId, pageable);
        Page<ActionResponse> responsePage = actionMapper.toDto(actions);
        return success(responsePage);
    }

    @Override
    @Transactional
    public void setLeadEditionAction(T lead, Transition transition) {
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
                .lead(lead)
                .build();

        editAction.setLeadDetails(editDetails);

        // === Persist via cascade ===
        userActionRepository.save(editAction);

        // === Optional: update updated at ===
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(editAction);
        leadRepository.save(lead);
    }

    @Override
    @Transactional
    public void setDeletionAction(T lead, Transition transition) {
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
                .lead(lead)
                .comment("Lead was deleted by " + deleter.getName())
                .build();

        deleteAction.setLeadDetails(deleteDetails);

        // === Save action with cascade to details ===
        userActionRepository.save(deleteAction);

        // === Optional: update lastActionDate if lead is not being physically deleted ===
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(deleteAction);
        leadRepository.save(lead);
    }

    @Override
    @Transactional
    public void setLeadRestoreAction(T lead, Transition transition) {
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
                .lead(lead)
                .build();

        restoreAction.setLeadDetails(restoreDetails);

        // === Save the action and details ===
        userActionRepository.save(restoreAction);

        // === Optionally update the lead metadata ===
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(restoreAction);
        leadRepository.save(lead);
    }

    // Will saved into database
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
    public void setDelayedAction(T lead, Transition transition) {
        User systemUser = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        // Create DELAYED action
        UserAction delayedAction = UserAction.builder()
                .creator(systemUser)
                .type(ActionType.DELAYED)
                .description("Lead was delayed due to no timely action by sales")
                .createdAt(LocalDateTime.now())
                .build();

        // LeadActionDetails
        LeadActionDetails delayedDetails = LeadActionDetails.builder()
                .userAction(delayedAction)
                .lead(lead)
                .comment("No action was taken on time; lead marked as Delayed")
                .build();

        delayedAction.setLeadDetails(delayedDetails);

        userActionRepository.save(delayedAction);

        // Update metadata
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(delayedAction);
        leadRepository.save(lead);
    }



}
