package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.CancelReasonsDTO;
import org.springframework.http.ResponseEntity;

public interface CancelReasonsService {
    ResponseEntity<?> getCancelReasons(int page, int size, Transition transition);

    ResponseEntity<?> getCancelReasonById(long id, Transition transition);

    ResponseEntity<?> createCancelReason(CancelReasonsDTO cancelReasonsDTO, Transition transition);

    ResponseEntity<?> updateCancelReason(CancelReasonsDTO cancelReasonsDTO, Transition transition);

    ResponseEntity<?> deleteCancelReason(long id, Transition transition);
}
