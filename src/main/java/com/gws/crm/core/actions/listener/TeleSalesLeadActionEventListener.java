package com.gws.crm.core.actions.listener;

import com.gws.crm.core.actions.event.lead.*;
import com.gws.crm.core.actions.event.telesales.*;
import com.gws.crm.core.actions.service.imp.TeleSalesLeadActionServiceImp;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TeleSalesLeadActionEventListener {

    private final TeleSalesLeadActionServiceImp actionService;

    @Async
    @EventListener
    public void onLeadCreated(TeleSalesLeadCreatedEvent event) {
        actionService.setLeadCreationAction(event.lead(), event.transition());
    }


    @Async
    @EventListener
    public void onLeadDeleted(TeleSalesLeadDeletedEvent event) {
        actionService.setDeletionAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadEdited(TeleSalesLeadEditedEvent event) {
        actionService.setLeadEditionAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadRestored(TeleSalesLeadRestoredEvent event) {
        actionService.setLeadRestoreAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadAssigned(TeleSalesLeadAssignedEvent event) {
        actionService.setAssignAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadViewed(TeleSalesLeadViewedEvent event) {
        actionService.setViewLeadAction(event.lead(), event.transition());
    }

    @Async
    @EventListener
    public void onLeadDelayed(TeleSalesLeadDelayedEvent event) {
        actionService.setDelayedAction(event.lead(), event.transition());
    }
}
