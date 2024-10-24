package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.entity.User;
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

    private final EmployeeRepository employeeRepository;
    private final SalesLeadRepository<T> leadRepository;
    private final CallOutcomeRepository callOutcomeRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final StageRepository stageRepository;

    @Autowired
    private ActionOnLeadRepository actionOnLeadRepository;
    @Autowired
    private ActionMapper actionMapper;

    protected ActionServiceImp(EmployeeRepository employeeRepository, SalesLeadRepository<T> leadRepository, CallOutcomeRepository callOutcomeRepository, CancelReasonsRepository cancelReasonsRepository, StageRepository stageRepository) {
        this.employeeRepository = employeeRepository;
        this.leadRepository = leadRepository;
        this.callOutcomeRepository = callOutcomeRepository;
        this.cancelReasonsRepository = cancelReasonsRepository;
        this.stageRepository = stageRepository;
    }


    @Override
    public ResponseEntity<?> setActionOnLead(ActionOnLeadDTO actionDTO, Transition transition) {
        log.info(actionDTO.toString());

        Employee creator = employeeRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = leadRepository.findById(actionDTO.getLeadId())
                .orElseThrow(NotFoundResourceException::new);

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .nextActionDate(actionDTO.getNextActionDate())
                .callBackTime(actionDTO.getCallBackTime())
                .comment(actionDTO.getComment())
                .type(actionDTO.getActionType().equals("answered") ? ActionType.ANSWERED : ActionType.NO_ANSWER)
                .createdAt(LocalDateTime.now());

        if (actionDTO.getCallOutcome() != null) {
            CallOutcome outcome = callOutcomeRepository.getReferenceById(actionDTO.getCallOutcome());
            String outcomeDescription = generateDescriptionBasedOnOutcome(outcome.getName(), creator, lead);
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
        List<ActionOnLead> actionOnLeadList = actionOnLeadRepository.getAllByLeadIdAndOrderByCreatedAtDesc(leadId);
        List<ActionResponse> actionResponsesList = actionMapper.toDto(actionOnLeadList);
        return success(actionResponsesList);
    }

    @Override
    public void setLeadCreationAction(SalesLead salesLead, Transition transition) {
        Employee creator = employeeRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;
        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.CREATE)
                .description(creator.getName() + " create Lead")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        if (lead.getSalesRep() != null) {
            ActionOnLead.ActionOnLeadBuilder assignActionBuilder = ActionOnLead.builder()
                    .creator(creator)
                    .lead(lead)
                    .type(ActionType.EDIT)
                    .description(creator.getName() + " update Lead")
                    .createdAt(LocalDateTime.now());

            ActionOnLead editAction = assignActionBuilder.build();
            lead.getActions().add(editAction);
        }
        lead.getActions().add(actionOnLead);
        leadRepository.save(lead);
    }


    @Transactional
    @Override
    public void setLeadEditAction(SalesLead salesLead, Transition transition) {
        Employee creator = employeeRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;
        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.ASSIGN)
                .description(creator.getName() + " Assign Lead for " + lead.getSalesRep().getName())
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }


    @Transactional
    @Override
    public void setSalesAssignAction(SalesLead salesLead, Transition transition) {
        Employee creator = employeeRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;
        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.ASSIGN)
                .description(creator.getName() + " update Lead")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }

    private String generateDescriptionBasedOnOutcome(String outcome, User creator, T lead) {
        return switch (outcome) {
            case "Call Back Later" ->
                    "Call back scheduled by " + creator.getName() + " for lead (Lead ID: " + lead.getId() + ").";
            case "Meeting Scheduled" ->
                    "Meeting scheduled by " + creator.getName() + " for lead (Lead ID: " + lead.getId() + ").";
            case "Information Sent" ->
                    "Information sent by " + creator.getName() + " to lead (Lead ID: " + lead.getId() + ").";
            case "Request for More Information" ->
                    "Lead (Lead ID: " + lead.getId() + ") requested more information. Handled by " + creator.getName() + ".";
            case "Cancellation" -> "Lead (Lead ID: " + lead.getId() + ") canceled by " + creator.getName() + ".";
            default -> "Action taken by " + creator.getName() + " for lead (Lead ID: " + lead.getId() + ").";
        };
    }


    @Override
    public void setSalesViewLeadAction(SalesLead salesLead, Transition transition) {
        Employee creator = employeeRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.VIEW)
                .description(creator.getName() + " viewed the lead")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }


    @Override
    public void setLeadDeletionAction(SalesLead salesLead, Transition transition) {
        Employee creator = employeeRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.DELETE)
                .description(creator.getName() + " deleted the lead")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }


    @Override
    public void setLeadRestoreAction(SalesLead salesLead, Transition transition) {
        Employee creator = employeeRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        T lead = (T) salesLead;

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .lead(lead)
                .type(ActionType.RESTORE)
                .description(creator.getName() + " restore the lead")
                .createdAt(LocalDateTime.now());

        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);

        leadRepository.save(lead);
    }

}
