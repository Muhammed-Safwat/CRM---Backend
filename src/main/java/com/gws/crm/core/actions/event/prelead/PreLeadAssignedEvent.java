// LeadAssignedEvent.java
package com.gws.crm.core.actions.event.prelead;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.PreLead;

public record PreLeadAssignedEvent(PreLead lead, Transition transition) {
}
