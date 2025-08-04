// LeadEditedEvent.java
package com.gws.crm.core.actions.event.prelead;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.PreLead;

public record PreLeadEditedEvent (PreLead lead, Transition transition) {
}
