package com.gws.crm.core.export.service;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.export.dtos.ExportCritira;
import com.gws.crm.core.export.dtos.ExportRequestDto;
import com.gws.crm.core.export.dtos.ExportResponseDto;
import com.gws.crm.core.export.entity.ExportStatus;
import com.gws.crm.core.export.entity.ExportTask;
import com.gws.crm.core.export.mapper.ExportTaskMapper;
import com.gws.crm.core.export.repository.ExportTaskRepository;
import com.gws.crm.core.export.spcification.ExportSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@AllArgsConstructor
@Slf4j
public class ExportOrchestrator {

    private final List<ExportHandler> handlers;
    private final ExportTaskRepository taskRepo;
    private final FileStorageService storage;
    private final UserRepository userRepository;
    private final ExportSpecification exportSpecification;
    private final ExportTaskMapper taskMapper;

    public ResponseEntity<?> queueExport(ExportRequestDto req, Transition transition) {
        log.info("=== STARTING EXPORT QUEUE ===");
        log.info("Request Type: {}, IDs count: {}, User ID: {}",
                req.getType(), req.getIds() != null ? req.getIds().size() : 0, transition.getUserId());

        User user = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        ExportTask task = ExportTask.builder()
                .referenceType(req.getReferenceType())
                .type(req.getType())
                .status(ExportStatus.QUEUED)
                .createdAt(LocalDateTime.now())
                .exportedBy(user)
                .exportIds(req.getIds())
                .build();

        String filename = req.getType() + "_export_" + UUID.randomUUID() + ".xlsx";
        task.setFilename(filename);

        taskRepo.save(task);

        log.info("Export task queued with ID: {}, Filename: {}", task.getId(), filename);
        log.info("=== EXPORT QUEUE COMPLETED ===");

        ExportResponseDto responseDto = taskMapper.mapToDto(task);
        return success(responseDto);
    }

    public ResponseEntity<?> processTaskImmediately(Long taskId) {
        log.info("=== PROCESSING TASK IMMEDIATELY FOR DEBUG ===");
        ExportTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        try {
            processTaskScheduled(task);
            ExportResponseDto responseDto = taskMapper.mapToDto(task);
            return success(responseDto);
        } catch (Exception e) {
            log.error("Failed to process task immediately", e);
            return ResponseEntity.internalServerError().body("Failed: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 60000)
    public void processQueuedTasks() {
        log.info("=== SCHEDULED TASK RUNNER STARTED ===");

        List<ExportTask> queuedTasks = taskRepo.findByStatus(ExportStatus.QUEUED);
        log.info("Found {} queued tasks", queuedTasks.size());

        for (ExportTask task : queuedTasks) {
            log.info("Submitting task ID: {} to async processor", task.getId());
            processTaskAsync(task);
        }

        log.info("=== SCHEDULED TASK RUNNER COMPLETED ===");
    }

    @Async
    public void processTaskAsync(ExportTask task) {
        try {
            processTaskScheduled(task);
        } catch (Exception e) {
            log.error("Error processing task {}: {}", task.getId(), e.getMessage(), e);
        }
    }

    @Transactional
    protected void processTaskScheduled(ExportTask task) throws Exception {
        log.info("=== PROCESSING TASK {} ===", task.getId());

        try {
            task.setStatus(ExportStatus.PROCESSING);
            taskRepo.save(task);

            ExportHandler handler = handlers.stream()
                    .filter(h -> h.supports(task.getReferenceType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("No handler found for referenceType '%s'. Available handlers: %s",
                                    task.getReferenceType(),
                                    handlers.stream()
                                            .map(h -> h.getClass().getSimpleName())
                                            .toList())));


            byte[] fileBytes = handler.generateFile(task.getExportIds(), Map.of());
            if (fileBytes == null || fileBytes.length == 0) {
                throw new RuntimeException("Generated file is empty or null");
            }

            String shortUuid = UUID.randomUUID().toString().substring(0, 8);

            String filename = shortUuid + ".xlsx";

            Long adminId = task.getExportedBy().getId();
            String typeFolder = task.getType().toLowerCase();
            String folderPath = "exports/" + adminId + "/" + typeFolder + "/";

            String fullPath = folderPath + filename;

            String filePath = storage.store(
                    fileBytes,
                    fullPath,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

            if (filePath == null || filePath.isEmpty()) {
                throw new RuntimeException("Storage service returned empty file path");
            }

            task.setFilename(filename);
            task.setFilePath(filePath);
            task.setSizeBytes((long) fileBytes.length);
            task.setStatus(ExportStatus.COMPLETED);
            task.setErrorMessage(null);
            taskRepo.save(task);

            log.info("=== TASK {} COMPLETED SUCCESSFULLY ===", task.getId());

        } catch (Exception e) {
            task.setStatus(ExportStatus.FAILED);
            task.setErrorMessage(e.getMessage());
            taskRepo.save(task);
            throw e;
        }
    }

    public ResponseEntity<?> getTask(Long id, Transition transition) {
        ExportTask task = taskRepo.findById(id)
                .orElseThrow(NotFoundResourceException::new);

        ExportResponseDto responseDto = taskMapper.mapToDto(task);
        return success(responseDto);
    }

    public ResponseEntity<?> getTasks(ExportCritira exportCritira, Transition transition) {
        Pageable page = PageRequest.of(
                exportCritira.getPage(),
                exportCritira.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<ExportTask> tasks = taskRepo.findAll(exportSpecification.filter(exportCritira, transition), page);

        Page<ExportResponseDto> responseDtos = tasks.map(taskMapper::mapToDto);
        return success(responseDtos);
    }
}
