package com.gws.crm.core.lockups.controller;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.dto.LockupDTO;
import com.gws.crm.core.lockups.service.CancelReasonsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/lockups/cancel-reasons")
@RequiredArgsConstructor
public class CancelReasonsController {

    private final CancelReasonsService cancelReasonsService;

    @GetMapping
    public ResponseEntity<?> getCancelReasons(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              Transition transition) {
        return cancelReasonsService.getCancelReasons(page, size ,transition);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCancelReasonById(@PathVariable Long id, Transition transition) {
        return cancelReasonsService.getCancelReasonById(id,transition);
    }

    @PostMapping
    public ResponseEntity<?> createCancelReason(@Valid @RequestBody LockupDTO cancelReasonsDTO, Transition transition) {
        return cancelReasonsService.createCancelReason(cancelReasonsDTO,transition);
    }

    @PutMapping
    public ResponseEntity<?> updateCancelReason(@RequestBody LockupDTO cancelReasonsDTO, Transition transition) {
        return cancelReasonsService.updateCancelReason(cancelReasonsDTO,transition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCancelReason(@PathVariable long id, Transition transition) {
        return cancelReasonsService.deleteCancelReason(id,transition);
    }
}
