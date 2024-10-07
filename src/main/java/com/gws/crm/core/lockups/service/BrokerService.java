package com.gws.crm.core.lockups.service;


import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.entity.Broker;

import java.util.List;

public interface BrokerService {
   List<Broker> findAllBroker(Transition transition);
}
