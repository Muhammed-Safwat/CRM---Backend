package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.StageDto;
import org.springframework.http.ResponseEntity;

public interface StageService {
    ResponseEntity<?> getStage(int page, int size, Transition transition);

    ResponseEntity<?> getStageById(long id, Transition transition);

    ResponseEntity<?> createStage(StageDto stageDto, Transition transition);

    ResponseEntity<?> updateStage(StageDto stageDto, Transition transition);

    ResponseEntity<?> deleteStage(long id, Transition transition);
}
