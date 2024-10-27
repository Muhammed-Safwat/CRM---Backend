package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.employee.dto.ActionResponse;
import com.gws.crm.core.employee.entity.ActionOnLead;
import com.gws.crm.core.employee.entity.ActionType;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.mapper.ActionMapper;
import com.gws.crm.core.employee.repository.ActionOnLeadRepository;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.employee.service.ActionService;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.repository.SalesLeadRepository;
import com.gws.crm.core.lookups.entity.CallOutcome;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Transactional
@Log
public abstract class ActionServiceImp<T extends SalesLead> implements ActionService<T> {

    private final UserRepository userRepository;
    private final SalesLeadRepository<T> leadRepository;
    private final CallOutcomeRepository callOutcomeRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final StageRepository stageRepository;

    @Autowired
    private ActionOnLeadRepository actionOnLeadRepository;

    @Autowired
    private ActionMapper actionMapper;

    protected ActionServiceImp(UserRepository userRepository, SalesLeadRepository<T> leadRepository,
                               CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        this.userRepository = userRepository;
        this.leadRepository = leadRepository;
        this.callOutcomeRepository = callOutcomeRepository;
        this.cancelReasonsRepository = cancelReasonsRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public ResponseEntity<?> setActionOnLead(ActionOnLeadDTO actionDTO, Transition transition) {
        log.info(actionDTO.toString());

        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = leadRepository.findById(actionDTO.getLeadId())
                .orElseThrow(NotFoundResourceException::new);
        String commentStr = actionDTO.getComment() != null
                ? actionDTO.getComment()
                : "No comment provided";
        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .nextActionDate(actionDTO.getNextActionDate())
                .callBackTime(actionDTO.getCallBackTime())
                .comment(commentStr)
                .type(actionDTO.getActionType().equals("answered") ? ActionType.ANSWERED : ActionType.NO_ANSWER)
                .createdAt(LocalDateTime.now());

        if (actionDTO.getActionType().equals("noAnswer")) {
            String description = "No Answer recorded. Callback scheduled";
            actionOnLeadBuilder.description(description);
        } else if (actionDTO.getCallOutcome() != null && actionDTO.getActionType().equals("answered")) {
            CallOutcome outcome = callOutcomeRepository.getReferenceById(actionDTO.getCallOutcome());
            String outcomeDescription = generateDescriptionBasedOnOutcome(outcome.getName());
            actionOnLeadBuilder.callOutcome(outcome)
                    .description(outcomeDescription);
        }

        if (actionDTO.getCancellationReason() != null) {
            CancelReasons cancelReasons = cancelReasonsRepository.getReferenceById(actionDTO.getCancellationReason());
            actionOnLeadBuilder.cancellationReason(cancelReasons.getName());
            lead.setCancelReasons(cancelReasons);
        }

        if (actionDTO.getStage() != null) {
            Stage stage = stageRepository.getReferenceById(actionDTO.getStage());
            actionOnLeadBuilder.stage(stage.getName());
            lead.setLastStage(stage.getName());
        }

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);

        return success("Action Added");
    }

    @Override
    public ResponseEntity<?> getActions(long leadId, Transition transition) {
        List<ActionOnLead> actionOnLeadList = actionOnLeadRepository.getAllByLeadIdOrderByCreatedAtAsc(leadId);
        List<ActionResponse> actionResponsesList = actionMapper.toDto(actionOnLeadList);
        return success(actionResponsesList);
    }

    @Override
    public void setLeadCreationAction(SalesLead salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;
        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.CREATE)
                .description("Created this lead")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        if (lead.getSalesRep() != null) {
            ActionOnLead.ActionOnLeadBuilder assignActionBuilder = ActionOnLead.builder()
                    .creator(creator)
                    .lead(lead)
                    .type(ActionType.ASSIGN)
                    .description("Assigned lead to " + lead.getSalesRep().getName())
                    .createdAt(LocalDateTime.now().plusSeconds(1));

            ActionOnLead editAction = assignActionBuilder.build();
            lead.getActions().add(editAction);
        }

        leadRepository.save(lead);
    }


    @Transactional
    @Override
    public void setLeadEditAction(SalesLead salesLead, Transition transition) {
        User editor = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;
        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(editor)
                .lead(lead)
                .type(ActionType.EDIT)
                .description("Updated lead details")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }


    @Transactional
    @Override
    public void setSalesAssignAction(SalesLead salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;
        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.ASSIGN)
                .description("Assign Lead for " + lead.getSalesRep().getName())
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }

    private String generateDescriptionBasedOnOutcome(String outcome) {
        StringBuilder description = new StringBuilder();
        description.append("Recorded an outcome: ").append(outcome) ;

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
    public void setSalesViewLeadAction(SalesLead salesLead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.VIEW)
                .description("Viewed the lead")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }


    @Override
    public void setLeadDeletionAction(SalesLead salesLead, Transition transition) {
        User deleter = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(deleter)
                .lead(lead)
                .type(ActionType.DELETE)
                .description("Deleted the lead record")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }

    @Override
    public void setLeadRestoreAction(SalesLead salesLead, Transition transition) {
        User restorer = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(restorer)
                .lead(lead)
                .type(ActionType.RESTORE)
                .description("Restored the lead record")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }


}
