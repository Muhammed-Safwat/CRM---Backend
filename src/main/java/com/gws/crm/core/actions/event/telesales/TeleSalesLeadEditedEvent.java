// LeadEditedEvent.java
package com.gws.crm.core.actions.event.telesales;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.TeleSalesLead;

public record TeleSalesLeadEditedEvent(TeleSalesLead lead, Transition transition) {
}
