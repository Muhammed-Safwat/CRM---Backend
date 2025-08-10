// LeadDelayedEvent.java
package com.gws.crm.core.actions.event.lead;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.Lead;

public record LeadDelayedEvent(Lead lead, Transition transition) {
}
