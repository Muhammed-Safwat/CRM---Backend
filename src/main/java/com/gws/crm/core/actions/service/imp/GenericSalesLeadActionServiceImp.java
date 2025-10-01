package com.gws.crm.core.actions.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.actions.dtos.ActionOnLeadDTO;
import com.gws.crm.core.actions.dtos.ActionResponse;
import com.gws.crm.core.actions.dtos.ActionCriteria;
import com.gws.crm.core.actions.entity.ActionType;
import com.gws.crm.core.actions.entity.LeadActionDetails;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.actions.mapper.ActionMapper;
import com.gws.crm.core.actions.repository.ActionDetailsRepository;
import com.gws.crm.core.actions.repository.UserActionRepository;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.repository.GenericSalesLeadRepository;
import com.gws.crm.core.lookups.entity.CallOutcome;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import lombok.extern.java.Log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Log
public abstract class GenericSalesLeadActionServiceImp<T extends SalesLead>
        extends GenericLeadActionServiceImp<T> {

    protected final UserRepository userRepository;
    protected final GenericSalesLeadRepository<T> leadRepository;
    protected final UserActionRepository userActionRepository;
    protected final ActionMapper actionMapper;
    protected final CallOutcomeRepository callOutcomeRepository;
    protected final CancelReasonsRepository cancelReasonsRepository;
    protected final StageRepository stageRepository;
    protected final ActionDetailsRepository actionDetailsRepository;

    protected GenericSalesLeadActionServiceImp(UserRepository userRepository,
            GenericSalesLeadRepository<T> leadRepository,
            UserActionRepository userActionRepository, ActionMapper actionMapper,
            CallOutcomeRepository callOutcomeRepository,
            CancelReasonsRepository cancelReasonsRepository,
            StageRepository stageRepository,
            ActionDetailsRepository actionDetailsRepository) {
        super(userRepository, leadRepository, userActionRepository, actionMapper);
        this.userRepository = userRepository;
        this.leadRepository = leadRepository;
        this.userActionRepository = userActionRepository;
        this.actionMapper = actionMapper;
        this.callOutcomeRepository = callOutcomeRepository;
        this.cancelReasonsRepository = cancelReasonsRepository;
        this.stageRepository = stageRepository;
        this.actionDetailsRepository = actionDetailsRepository;
    }

    @Override
    @Transactional
    public void setAssignAction(T salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        UserAction assignAction = UserAction.builder()
                .creator(creator)
                .type(ActionType.ASSIGN)
                .description("Assigned lead to " + salesLead.getSalesRep().getName())
                .createdAt(LocalDateTime.now())
                .build();

        LeadActionDetails assignDetails = LeadActionDetails.builder()
                .userAction(assignAction)
                .lead(salesLead)
                .build();

        assignAction.setLeadDetails(assignDetails);

        userActionRepository.save(assignAction);

        salesLead.setLastActionDate(LocalDateTime.now());
        salesLead.setUpdatedAt(LocalDateTime.now());
        salesLead.getActions().add(assignAction);

        leadRepository.save(salesLead);
    }

    @Override
    @Transactional
    public void setViewLeadAction(T salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        UserAction viewAction = UserAction.builder()
                .creator(creator)
                .type(ActionType.VIEW)
                .description("Viewed the lead")
                .createdAt(LocalDateTime.now())
                .build();

        LeadActionDetails viewDetails = LeadActionDetails.builder()
                .userAction(viewAction)
                .lead(salesLead)
                .comment("Lead was viewed by " + creator.getName())
                .build();

        viewAction.setLeadDetails(viewDetails);

        userActionRepository.save(viewAction);

        salesLead.setLastActionDate(LocalDateTime.now());
        salesLead.setUpdatedAt(LocalDateTime.now());
        salesLead.getActions().add(viewAction);

        leadRepository.save(salesLead);
    }

    @Transactional
    public void setLeadCreationAction(List<T> leads, Transition transition) {
        for (T lead : leads) {
            setLeadCreationAction(lead, transition);
        }
    }

    @Override
    @Transactional
    public void setLeadCreationAction(T salesLead, Transition transition) {
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
        UserAction createAction = UserAction.builder()
                .creator(salesLead.getCreator())
                .type(ActionType.CREATE)
                .description("Created this lead")
                .createdAt(LocalDateTime.now())
                .build();

        LeadActionDetails createDetails = LeadActionDetails.builder()
                .userAction(createAction)
                .lead(salesLead)
                .comment("Initial creation")
                .build();

        createAction.setLeadDetails(createDetails);

        UserAction assignAction = null;
        if (salesLead.getSalesRep() != null) {
            assignAction = UserAction.builder()
                    .creator(salesLead.getCreator())
                    .type(ActionType.ASSIGN)
                    .description("Assigned lead to " + salesLead.getSalesRep().getName())
                    .createdAt(LocalDateTime.now().plusSeconds(1))
                    .build();

            LeadActionDetails assignDetails = LeadActionDetails.builder()
                    .userAction(assignAction)
                    .lead(salesLead)
                    .comment("Auto-assigned during creation")
                    .build();

            assignAction.setLeadDetails(assignDetails);
        }

        userActionRepository.save(createAction);
        if (assignAction != null) {
            userActionRepository.save(assignAction);
        }

        salesLead.setLastActionDate(LocalDateTime.now());
        salesLead.setUpdatedAt(LocalDateTime.now());
        salesLead.getActions().add(createAction);
        salesLead.getActions().add(assignAction);

        leadRepository.save(salesLead);
    }

    @Transactional
    public ResponseEntity<?> setActionOnSalesLead(ActionOnLeadDTO actionDTO, Transition transition) {
        log.info(actionDTO.toString());

        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = leadRepository.findById(actionDTO.getLeadId())
                .orElseThrow(NotFoundResourceException::new);

        String leadName = lead.getName();
        ActionType actionType = ActionType.NO_ANSWER;

        if (actionDTO.isAnswer()) {
            actionType = ActionType.ANSWERED;
        }

        String description;
        CallOutcome outcome = null;

        if (actionType == ActionType.ANSWERED && actionDTO.getNextAction() != null) {
            outcome = callOutcomeRepository.getReferenceById(actionDTO.getNextAction());
            description = "Answered call for lead: " + leadName + " - Outcome: " + outcome.getName();
        } else if (actionType == ActionType.NO_ANSWER) {
            description = "No answer from lead: " + leadName + ". Callback scheduled.";
        } else {
            description = "Performed " + actionType.name() + " on lead: " + leadName;
        }

        String commentStr = (actionDTO.getComment() != null && !actionDTO.getComment().isBlank())
                ? actionDTO.getComment()
                : "User " + creator.getName() + " performed " + actionType.name() + " on lead " + leadName;

        UserAction action = UserAction.builder()
                .creator(creator)
                .type(actionType)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();


        LeadActionDetails leadDetails = LeadActionDetails.builder()
                .userAction(action)
                .lead(lead)
                .callOutcome(outcome)
                .nextActionDate(actionDTO.getNextActionDate())
                .callBackTime(actionDTO.getCallBackTime())
                .comment(commentStr)
                .build();

        if (actionDTO.getCancellationReason() != null) {
            CancelReasons cancelReasons = cancelReasonsRepository.getReferenceById(actionDTO.getCancellationReason());
            leadDetails.setCancellationReason(cancelReasons.getName());
            lead.setCancelReasons(cancelReasons);
        }

        // Optional: Stage update
        if (actionDTO.getStage() != null) {
            Stage stage = stageRepository.getReferenceById(actionDTO.getStage());
            leadDetails.setStage(stage.getName());
            lead.setLastStage(stage.getName());
        }


        action.setLeadDetails(leadDetails);
        userActionRepository.save(action);

        lead.setLastActionDate(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(action);
        lead.setDelay(false);
        leadRepository.save(lead);

        return success("Action added successfully.");
    }



}
