package com.gws.crm.core.actions.listener;

import com.gws.crm.core.actions.event.lead.*;
import com.gws.crm.core.actions.service.imp.SalesLeadActionServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesLeadActionEventListener {

    private final SalesLeadActionServiceImp actionService;

    @Async
    @EventListener
    public void onLeadCreated(LeadCreatedEvent event) {
        log.info("Event Reached here !!!!!!!!!!!!!!!!!!!!!");
        actionService.setLeadCreationAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadDeleted(LeadDeletedEvent event) {
        actionService.setDeletionAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadEdited(LeadEditedEvent event) {
        actionService.setLeadEditionAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadRestored(LeadRestoredEvent event) {
        actionService.setLeadRestoreAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadAssigned(LeadAssignedEvent event) {
        actionService.setAssignAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadViewed(LeadViewedEvent event) {
        actionService.setViewLeadAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadDelayed(LeadDelayedEvent event) {
        actionService.setDelayedAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadCreated(LeadsCreatedEvent event) {
        actionService.setLeadCreationAction(event.leads(), event.transition());
    }
}
