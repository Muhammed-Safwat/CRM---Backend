package com.gws.crm.core.resale.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.resale.dto.AddResaleDTO;
import com.gws.crm.core.resale.dto.ImportResaleDTO;
import com.gws.crm.core.resale.dto.ResaleCriteria;
import com.gws.crm.core.resale.repository.ResaleRepository;
import com.gws.crm.core.resale.service.ResaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResaleServiceImp implements ResaleService {


    private final ResaleRepository resaleRepository;
    private final ResaleMapper resaleMapper;


    @Override
    public ResponseEntity<?> getResales(int page, int size, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAllResales(ResaleCriteria resaleCriteria, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> getResaleDetails(long resaleId, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> addResale(AddResaleDTO resaleDTO, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateResale(AddResaleDTO resaleDTO, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteResale(Long resaleId, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> restoreResale(Long resaleId, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> generateExcel(Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> importResale(List<ImportResaleDTO> resales, Transition transition) {
        return null;
    }
}
