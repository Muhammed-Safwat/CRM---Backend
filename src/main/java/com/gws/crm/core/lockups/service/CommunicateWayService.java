package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.LockupDTO;
import org.springframework.http.ResponseEntity;

public interface CommunicateWayService {
    ResponseEntity<?> getCommunicateWays(int page, int size, Transition transition);

    ResponseEntity<?> getCommunicateWayById(long id, Transition transition);

    ResponseEntity<?> createCommunicateWay(LockupDTO communicateWayDTO, Transition transition);

    ResponseEntity<?> updateCommunicateWay(LockupDTO communicateWayDTO, Transition transition);

    ResponseEntity<?> deleteCommunicateWay(long id, Transition transition);
}
