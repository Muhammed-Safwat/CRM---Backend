package com.gws.crm.core.employee.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.dto.ActionOnLeadDTO;
import com.gws.crm.core.employee.entity.ActionOnLead;
import com.gws.crm.core.employee.entity.ActionType;
import com.gws.crm.core.employee.service.ActionService;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.repository.SalesLeadRepository;
import com.gws.crm.core.lookups.entity.CancelReasons;
import com.gws.crm.core.lookups.entity.Stage;
import com.gws.crm.core.lookups.repository.CallOutcomeRepository;
import com.gws.crm.core.lookups.repository.CancelReasonsRepository;
import com.gws.crm.core.lookups.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.gws.crm.common.handler.ApiResponseHandler.badRequest;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
@Log
public class ActionServiceImp implements ActionService {

    private final UserRepository userRepository;
    private final SalesLeadRepository leadRepository;
    private final CallOutcomeRepository callOutcomeRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final StageRepository stageRepository;

    @Override
    public ResponseEntity<?> setActionOnLead(ActionOnLeadDTO actionDTO, Transition transition) {
        log.info(actionDTO.toString());
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        SalesLead lead = leadRepository.findById(actionDTO.getLeadId())
                .orElseThrow(NotFoundResourceException::new);
        /*
            if(lead.getAdmin().getId() != creator.getId() && lead.get().getId() != creator.getId()) {
                badRequest();
            }
        */

        ActionOnLead.ActionOnLeadBuilder actionOnLeadBuilder = ActionOnLead.builder()
                .creator(creator)
                .baseLead(lead)
                .nextActionDate(actionDTO.getNextActionDate())
                .callBackTime(actionDTO.getCallBackTime())
                .comment(actionDTO.getComment())
                .type(actionDTO.getActionType().equals("answered")? ActionType.ANSWERED: ActionType.NO_ANSWER)
                .createdAt(LocalDateTime.now());

        if(actionDTO.getCallOutcome() != null){
            actionOnLeadBuilder.callOutcome(callOutcomeRepository.getReferenceById(actionDTO.getCallOutcome()));
        }

        CancelReasons cancelReasons = null;
        if(actionDTO.getCancellationReason() != null){
            cancelReasons = cancelReasonsRepository.getReferenceById(actionDTO.getCancellationReason());
            actionOnLeadBuilder.cancellationReason(cancelReasons.getName());
            lead.setCancelReasons(cancelReasons);
        }

        Stage stage = null;
        if(actionDTO.getStage() != null){
            stage = stageRepository.getReferenceById(actionDTO.getStage());
            actionOnLeadBuilder.stage(stage.getName());
            lead.setLastStage(stage.getName());
        }
        ActionOnLead actionOnLead = actionOnLeadBuilder.build();
        lead.getActions().add(actionOnLead);
        leadRepository.save(lead);

        return success("Action Added");
    }

}
