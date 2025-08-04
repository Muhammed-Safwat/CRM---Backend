package com.gws.crm.core.actions.listener;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.actions.event.*;
import com.gws.crm.core.actions.event.lead.*;
import com.gws.crm.core.actions.event.prelead.*;
import com.gws.crm.core.actions.service.imp.PreLeadActionServiceImp;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.leads.repository.PreLeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PreLeadActionEventListener extends LeadActionEventListener<PreLead> {

    private final PreLeadActionServiceImp actionService;


    @Async
    @EventListener
    public void onLeadCreated(TestEvent event) {
        log.info("On Lead Creation ****************************");
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onLeadCreated(PreLeadCreatedEvent event) {
        log.info("On Lead Creation ****************************");
        actionService.setLeadCreationAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadDeleted(PreLeadDeletedEvent event) {
        actionService.setDeletionAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadEdited(PreLeadEditedEvent event) {
        actionService.setLeadEditionAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadRestored(PreLeadRestoredEvent event) {
        actionService.setLeadRestoreAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadAssigned(PreLeadAssignedEvent event) {
        actionService.setAssignAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadViewed(PreLeadViewedEvent event) {
        actionService.setViewLeadAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadDelayed(PreLeadDelayedEvent event) {
        actionService.setDelayedAction(event.lead(), event.transition());
    }




}
