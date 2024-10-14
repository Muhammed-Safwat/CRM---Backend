package com.gws.crm.core.lookups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lookups.dto.LockupDTO;
import com.gws.crm.core.lookups.entity.BaseLookup;
import org.springframework.http.ResponseEntity;

public interface BaseLookupService<T extends BaseLookup, D extends LockupDTO> {

    ResponseEntity<?> getAll(int page, int size, Transition transition);

    ResponseEntity<?> getById(long id, Transition transition);

    ResponseEntity<?> create(D dto, Transition transition);

    ResponseEntity<?> update(D dto, Transition transition);

    ResponseEntity<?> delete(long id, Transition transition);
}