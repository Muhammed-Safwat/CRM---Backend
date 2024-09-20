package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.lockups.dto.StageDto;
import com.gws.crm.core.lockups.entity.Stage;
import com.gws.crm.core.lockups.repository.StageRepository;
import com.gws.crm.core.lockups.service.StageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Log
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;

    @Override
    public ResponseEntity<?> getStage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Stage> stagePage = stageRepository.findAll(pageable);
        return success(stagePage);
    }

    @Override
    public ResponseEntity<?> getStageById(long id) {
        Stage stage = stageRepository.findById(id).orElseThrow(NotFoundResourceException::new);
        return success(stage);
    }

    @Override
    public ResponseEntity<?> createStage(StageDto stageDto) {
        Stage stage = Stage.builder().name(stageDto.getName()).build();
        Stage saveStage = stageRepository.save(stage);
        return success(saveStage);
    }

    @Override
    public ResponseEntity<?> updateStage(StageDto stageDto) {
        Stage stage = stageRepository.findById(stageDto.getId()).orElseThrow(NotFoundResourceException::new);
        stage.setName(stageDto.getName());
        stageRepository.save(stage);
        log.info(stage.getName() + "   =>TEeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        return success(stage);
    }

    @Override
    public ResponseEntity<?> deleteStage(long id) {
        stageRepository.deleteById(id);
        return success("Stage deleted successfully");
    }
}
