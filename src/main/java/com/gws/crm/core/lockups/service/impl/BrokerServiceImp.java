package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.entity.Broker;
import com.gws.crm.core.lockups.repository.BrokerRepository;
import com.gws.crm.core.lockups.service.BrokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrokerServiceImp implements BrokerService {
    private final BrokerRepository brokerRepository;
    @Override
    public List<Broker> findAllBroker(Transition transition) {
        return brokerRepository.findAllByAdminId(transition.getUserId());
    }
}
