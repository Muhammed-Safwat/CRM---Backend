package com.gws.crm.core.lockups.service;

import com.gws.crm.core.lockups.dto.StageDto;
import org.springframework.http.ResponseEntity;

public interface StageService {
    ResponseEntity<?> getStage(int page, int size);

    ResponseEntity<?> getStageById(long id);

    ResponseEntity<?> createStage(StageDto stageDto);

    ResponseEntity<?> updateStage(StageDto stageDto);

    ResponseEntity<?> deleteStage(long id);
}
