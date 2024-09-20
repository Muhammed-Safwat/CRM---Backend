package com.gws.crm.core.lockups.service;

import com.gws.crm.core.lockups.dto.CommunicateWayDTO;
import org.springframework.http.ResponseEntity;

public interface CommunicateWayService {
    ResponseEntity<?> getCommunicateWays(int page, int size);

    ResponseEntity<?> getCommunicateWayById(long id);

    ResponseEntity<?> createCommunicateWay(CommunicateWayDTO communicateWayDTO);

    ResponseEntity<?> updateCommunicateWay(CommunicateWayDTO communicateWayDTO);

    ResponseEntity<?> deleteCommunicateWay(long id);
}
