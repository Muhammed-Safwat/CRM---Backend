package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lockups.dto.CommunicateWayDTO;
import com.gws.crm.core.lockups.entity.CommunicateWay;
import com.gws.crm.core.lockups.repository.CommunicateWayRepository;
import com.gws.crm.core.lockups.service.CommunicateWayService;
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
public class CommunicateWayServiceImpl implements CommunicateWayService {

    private final CommunicateWayRepository communicateWayRepository;
    private final AdminRepository adminRepository;
    @Override
    public ResponseEntity<?> getCommunicateWays(int page, int size, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<CommunicateWay> communicateWaysPage = communicateWayRepository.findAllByAdminId(pageable, transition.getUserId());
        return success(communicateWaysPage);
    }

    @Override
    public ResponseEntity<?> getCommunicateWayById(long id, Transition transition) {
        CommunicateWay communicateWay = communicateWayRepository.findById(id).orElseThrow(NotFoundResourceException::new);
        return success(communicateWay);
    }

    @Override
    public ResponseEntity<?> createCommunicateWay(CommunicateWayDTO communicateWayDTO, Transition transition) {
        CommunicateWay communicateWay = CommunicateWay.builder()
                .admin(adminRepository.getReferenceById(transition.getUserId()))
                .name(communicateWayDTO.getName())
                .build();
        communicateWayRepository.save(communicateWay);
        return success(communicateWay);
    }

    @Override
    public ResponseEntity<?> updateCommunicateWay(CommunicateWayDTO communicateWayDTO, Transition transition) {
        CommunicateWay communicateWay = communicateWayRepository.findById(communicateWayDTO.getId()).orElseThrow(NotFoundResourceException::new);
        communicateWay.setName(communicateWay.getName());
        return success(communicateWay);
    }

    @Override
    public ResponseEntity<?> deleteCommunicateWay(long id, Transition transition) {
        communicateWayRepository.deleteById(id);
        return success();
    }
}
