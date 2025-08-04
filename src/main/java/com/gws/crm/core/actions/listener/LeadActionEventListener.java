package com.gws.crm.core.actions.listener;

import com.gws.crm.core.actions.event.lead.*;
import com.gws.crm.core.leads.entity.BaseLead;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public abstract class LeadActionEventListener<T extends BaseLead> {
/*
    @Async
    @EventListener
    abstract public void onLeadCreated(LeadCreatedEvent<T> event);

    @Async
    @EventListener
    abstract public void onLeadDeleted(LeadDeletedEvent<T> event);

    @Async
    @EventListener
    abstract public void onLeadEdited(LeadEditedEvent<T> event);

    @Async
    @EventListener
    abstract public void onLeadRestored(LeadRestoredEvent<T> event);

    @Async
    @EventListener
    abstract public void onLeadAssigned(LeadAssignedEvent<T> event);

    @Async
    @EventListener
    abstract public void onLeadViewed(LeadViewedEvent<T> event);

    @Async
    @EventListener
    abstract public void onLeadDelayed(LeadDelayedEvent<T> event);
*/}
