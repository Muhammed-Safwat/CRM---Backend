package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.employee.entity.ActionType;
import com.gws.crm.core.employee.entity.LeadActionDetails;
import com.gws.crm.core.employee.entity.UserAction;
import com.gws.crm.core.employee.mapper.ActionMapper;
import com.gws.crm.core.employee.repository.UserActionRepository;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.repository.GenericSalesLeadRepository;
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

    protected GenericSalesLeadActionServiceImp(UserRepository userRepository, GenericSalesLeadRepository<T> leadRepository, UserActionRepository userActionRepository, ActionMapper actionMapper, CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        super(userRepository, leadRepository, userActionRepository, actionMapper);
        this.userRepository = userRepository;
        this.leadRepository = leadRepository;
        this.userActionRepository = userActionRepository;
        this.actionMapper = actionMapper;
        this.callOutcomeRepository = callOutcomeRepository;
        this.cancelReasonsRepository = cancelReasonsRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> setActionOnLead(ActionOnLeadDTO actionDTO, Transition transition) {
        log.info(actionDTO.toString());

        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = leadRepository.findById(actionDTO.getLeadId())
                .orElseThrow(NotFoundResourceException::new);


        ActionType actionType = switch (actionDTO.getActionType().toLowerCase()) {
            case "answered" -> ActionType.ANSWERED;
            case "noanswer" -> ActionType.NO_ANSWER;
            default -> throw new IllegalArgumentException("Invalid action type");
        };

        String description = "Action recorded";

        CallOutcome outcome = null;
        if (actionType == ActionType.ANSWERED && actionDTO.getCallOutcome() != null) {
            outcome = callOutcomeRepository.getReferenceById(actionDTO.getCallOutcome());
            description = super.generateDescriptionBasedOnOutcome(outcome.getName());
        } else if (actionType == ActionType.NO_ANSWER) {
            description = "No Answer recorded. Callback scheduled.";
        }
        String commentStr = actionDTO.getComment() != null
                ? actionDTO.getComment()
                : "No comment provided";

        // Step 3: Create UserAction
        UserAction action = UserAction.builder()
                .creator(creator)
                .type(actionType)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        // Step 4: Build LeadActionDetails
        LeadActionDetails leadDetails = LeadActionDetails.builder()
                .userAction(action)
                .lead(lead)
                .callOutcome(outcome)
                .nextActionDate(actionDTO.getNextActionDate())
                .callBackTime(actionDTO.getCallBackTime())
                .build();

        if (actionDTO.getCancellationReason() != null) {
            CancelReasons cancelReasons = cancelReasonsRepository.getReferenceById(actionDTO.getCancellationReason());
            leadDetails.setCancellationReason(cancelReasons.getName());
            lead.setCancelReasons(cancelReasons);
        }

        if (actionDTO.getStage() != null) {
            Stage stage = stageRepository.getReferenceById(actionDTO.getStage());
            leadDetails.setStage(stage.getName());
            lead.setLastStage(stage.getName());
        }

        // Step 5: Set details and persist
        action.setLeadDetails(leadDetails);
        userActionRepository.save(action); // saves both action and leadDetails due to cascade

        lead.setLastActionDate(LocalDateTime.now());
        lead.getActions().add(action); // if you still keep actions list in SalesLead
        leadRepository.save(lead);

        return success("Action added successfully.");
    }

    @Override
    @Transactional
    public void setSalesAssignAction(T salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        // === Create UserAction (ASSIGN) ===
        UserAction assignAction = UserAction.builder()
                .creator(creator)
                .type(ActionType.ASSIGN)
                .description("Assigned lead to " + salesLead.getSalesRep().getName())
                .createdAt(LocalDateTime.now())
                .build();

        // === Create LeadActionDetails for UserAction ===
        LeadActionDetails assignDetails = LeadActionDetails.builder()
                .userAction(assignAction)
                .lead(salesLead)
                .build();

        assignAction.setLeadDetails(assignDetails);

        // === Save UserAction with cascaded LeadActionDetails ===
        userActionRepository.save(assignAction);

        // === Optional: update lead metadata ===
        salesLead.setLastActionDate(LocalDateTime.now());
        leadRepository.save(salesLead);
    }

    @Override
    @Transactional
    public void setSalesViewLeadAction(T salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        // === Create the VIEW action ===
        UserAction viewAction = UserAction.builder()
                .creator(creator)
                .type(ActionType.VIEW)
                .description("Viewed the lead")
                .createdAt(LocalDateTime.now())
                .build();

        // === Create the related LeadActionDetails ===
        LeadActionDetails viewDetails = LeadActionDetails.builder()
                .userAction(viewAction)
                .lead(salesLead)
                .comment("Lead was viewed by " + creator.getName())
                .build();

        viewAction.setLeadDetails(viewDetails);

        // === Save the action and details ===
        userActionRepository.save(viewAction);

        // === Optionally update the lead's last activity date ===
        salesLead.setLastActionDate(LocalDateTime.now());
        leadRepository.save(salesLead);
    }

    @Override
    @Transactional
    public void setSalesLeadCreationAction(T salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        // === Create Creation Action ===
        UserAction createAction = UserAction.builder()
                .creator(creator)
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

        // === Create Assign Action (optional) ===
        UserAction assignAction = null;
        if (salesLead.getSalesRep() != null) {
            assignAction = UserAction.builder()
                    .creator(creator)
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

        // === Save both actions ===
        userActionRepository.save(createAction); // Cascade saves leadDetails
        if (assignAction != null) {
            userActionRepository.save(assignAction);
        }

        // === Optional: Update lead timeline if needed ===
        salesLead.setLastActionDate(LocalDateTime.now());
        leadRepository.save(salesLead);
    }
}
