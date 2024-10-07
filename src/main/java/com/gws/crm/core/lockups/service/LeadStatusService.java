package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.entity.LeadStatus;

import java.util.List;

public interface LeadStatusService {

    List<LeadStatus> getAllLeadStatus(Transition transition);

}
