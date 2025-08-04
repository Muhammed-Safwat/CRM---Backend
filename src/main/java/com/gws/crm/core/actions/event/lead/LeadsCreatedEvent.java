package com.gws.crm.core.actions.event.lead;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.entity.Lead;

import java.util.List;

public record LeadsCreatedEvent(List<Lead> leads, Transition transition) {

}
