package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.repository.AdminRepository;
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
    private final AdminRepository adminRepository;
    @Override
    public ResponseEntity<?> getStage(int page, int size, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Stage> stagePage = stageRepository.findAllByAdminId(pageable,transition.getUserId());
        return success(stagePage);
    }

    @Override
    public ResponseEntity<?> getStageById(long id, Transition transition) {
        Stage stage = stageRepository.findByIdAndAdminId(id,transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        return success(stage);
    }

    @Override
    public ResponseEntity<?> createStage(StageDto stageDto, Transition transition) {
        Stage stage = Stage.builder()
                .admin(adminRepository.getReferenceById(transition.getUserId()))
                .name(stageDto.getName())
                .admin(adminRepository.getReferenceById(transition.getUserId()))
                .build();

        Stage saveStage = stageRepository.save(stage);
        return success(saveStage);
    }

    @Override
    public ResponseEntity<?> updateStage(StageDto stageDto, Transition transition) {
        Stage stage = stageRepository.findByIdAndAdminId(stageDto.getId(),transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        stage.setName(stageDto.getName());
        stageRepository.save(stage);
        log.info(stage.getName() + "   => TEeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        return success(stage);
    }

    @Override
    public ResponseEntity<?> deleteStage(long id, Transition transition) {
        stageRepository.deleteByIdAndAdminId(id,transition.getUserId());
        return success("Stage deleted successfully");
    }
}
