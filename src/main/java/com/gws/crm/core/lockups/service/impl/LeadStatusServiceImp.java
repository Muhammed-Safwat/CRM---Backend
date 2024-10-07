package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lockups.entity.LeadStatus;
import com.gws.crm.core.lockups.repository.LeadStatusRepository;
import com.gws.crm.core.lockups.service.LeadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadStatusServiceImp implements LeadStatusService {

    private final LeadStatusRepository leadStatusRepository;
    @Override
    public List<LeadStatus> getAllLeadStatus(Transition transition) {
        return leadStatusRepository.findAll();
    }

}
