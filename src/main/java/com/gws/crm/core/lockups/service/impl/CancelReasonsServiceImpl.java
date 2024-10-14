package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lockups.dto.LockupDTO;
import com.gws.crm.core.lockups.entity.CancelReasons;
import com.gws.crm.core.lockups.repository.CancelReasonsRepository;
import com.gws.crm.core.lockups.service.CancelReasonsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@RequiredArgsConstructor
public class CancelReasonsServiceImpl implements CancelReasonsService {

    private final CancelReasonsRepository cancelReasonsRepository;
    private final AdminRepository adminRepository;
    @Override
    public ResponseEntity<?> getCancelReasons(int page, int size, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<CancelReasons> cancelReasonsPage = cancelReasonsRepository.findAllByAdminId(pageable, transition.getUserId());
        return success(cancelReasonsPage);
    }

    @Override
    public ResponseEntity<?> getCancelReasonById(long id, Transition transition) {
        CancelReasons cancelReason = cancelReasonsRepository.findById(id)
                .orElseThrow(NotFoundResourceException::new);
        return success(cancelReason);
    }

    @Override
    public ResponseEntity<?> createCancelReason(LockupDTO cancelReasonsDTO, Transition transition) {
        CancelReasons cancelReason = CancelReasons.builder()
                .admin(adminRepository.getReferenceById(transition.getUserId()))
                .name(cancelReasonsDTO.getName())
                .build();
        CancelReasons savedCancelReason = cancelReasonsRepository.save(cancelReason);
        return success(savedCancelReason);
    }

    @Override
    public ResponseEntity<?> updateCancelReason(LockupDTO cancelReasonsDTO, Transition transition) {
        CancelReasons cancelReason = cancelReasonsRepository.findById(cancelReasonsDTO.getId())
                .orElseThrow(NotFoundResourceException::new);
        cancelReason.setName(cancelReasonsDTO.getName());
        cancelReasonsRepository.save(cancelReason);
        return success(cancelReason);
    }

    @Override
    public ResponseEntity<?> deleteCancelReason(long id, Transition transition) {
        cancelReasonsRepository.deleteById(id);
        return success("Cancel reason deleted successfully");
    }
}
