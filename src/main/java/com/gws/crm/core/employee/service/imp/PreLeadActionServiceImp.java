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
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.leads.repository.GenericBaseLeadRepository;
import com.gws.crm.core.leads.repository.PreLeadRepository;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Log
 public class PreLeadActionServiceImp extends GenericLeadActionServiceImp<PreLead> {

    protected final UserRepository userRepository;
    protected final PreLeadRepository leadRepository;
    protected final UserActionRepository userActionRepository;

    public PreLeadActionServiceImp(UserRepository userRepository,
                                   PreLeadRepository leadRepository,
                                   UserActionRepository userActionRepository,
                                   ActionMapper actionMapper) {
        super(userRepository, leadRepository, userActionRepository, actionMapper);
        this.userRepository = userRepository;
        this.leadRepository = leadRepository;
        this.userActionRepository = userActionRepository;
    }

    @Override
    @Transactional
    public void setLeadCreationAction(PreLead lead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        UserAction createAction = UserAction.builder()
                .creator(creator)
                .type(ActionType.CREATE)
                .description("Created PreLead")
                .createdAt(LocalDateTime.now())
                .build();

        LeadActionDetails createDetails = LeadActionDetails.builder()
                .userAction(createAction)
                .lead(lead)
                .comment("Initial creation")
                .build();

        createAction.setLeadDetails(createDetails);
        userActionRepository.save(createAction);

        lead.setLastActionDate(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(createAction);
        leadRepository.save(lead);
    }

    @Transactional
    @Override
    public void setLeadCreationAction(List<PreLead> leads, Transition transition) {
        for(PreLead lead : leads){
            this.setLeadCreationAction(lead,transition);
        }
    }


    @Transactional
    public void setAssignAction(List<PreLead> leads, Transition transition) {
        for (PreLead lead : leads) {
            setAssignAction(lead, transition);
        }
    }


    @Override
    @Transactional
    public void setAssignAction(PreLead lead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        UserAction assignAction = UserAction.builder()
                .creator(creator)
                .type(ActionType.ASSIGN)
                .description("Assigned PreLead to " + lead.getAssignedTo())
                .createdAt(LocalDateTime.now())
                .build();

        LeadActionDetails assignDetails = LeadActionDetails.builder()
                .userAction(assignAction)
                .lead(lead)
                .comment("Assignment recorded")
                .build();

        assignAction.setLeadDetails(assignDetails);
        userActionRepository.save(assignAction);

        lead.setLastActionDate(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(assignAction);
        leadRepository.save(lead);
    }

    @Override
    @Transactional
    public void setViewLeadAction(PreLead lead, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        UserAction viewAction = UserAction.builder()
                .creator(creator)
                .type(ActionType.VIEW)
                .description("Viewed PreLead")
                .createdAt(LocalDateTime.now())
                .build();

        LeadActionDetails viewDetails = LeadActionDetails.builder()
                .userAction(viewAction)
                .lead(lead)
                .comment("Viewed by " + creator.getName())
                .build();

        viewAction.setLeadDetails(viewDetails);
        UserAction userActions = userActionRepository.save(viewAction);

        lead.setLastActionDate(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());
        lead.getActions().add(userActions);
        leadRepository.save(lead);
    }


    @Transactional
    public void setImportLeads(List<PreLead> savedLeads, Transition transition){
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        for (PreLead lead : savedLeads) {
            UserAction importAction = UserAction.builder()
                    .creator(creator)
                    .type(ActionType.IMPORT_DATA)
                    .description("Imported lead")
                    .createdAt(LocalDateTime.now())
                    .build();

            LeadActionDetails importDetails = LeadActionDetails.builder()
                    .userAction(importAction)
                    .lead(lead)
                    .comment("Imported via file upload")
                    .build();

            importAction.setLeadDetails(importDetails);

            userActionRepository.save(importAction);

            lead.setLastActionDate(LocalDateTime.now());
            lead.setImportedAt(LocalDateTime.now());
            lead.setImportedBy(creator.getName());
            // lead.setAssignedTo();
            lead.setImported(true);
            lead.getActions().add(importAction);
            leadRepository.save(lead);
        }
    }


}
