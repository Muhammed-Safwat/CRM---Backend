package com.gws.crm.core.actions.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.dtos.ActionResponse;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.actions.mapper.ActionMapper;
import com.gws.crm.core.actions.repository.UserActionRepository;
import com.gws.crm.core.actions.service.UserActionService;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.badRequest;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Log
@AllArgsConstructor
public class UserActionServiceImp implements UserActionService {

    private final UserActionRepository userActionRepository;

    private final ActionMapper actionMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<?> getActions(long userId, int page, int size, Transition transition) {
        boolean isSubordinate = employeeRepository.isSubordinate(userId, transition.getUserId()) > 0;

        log.info("{} ================ " + transition.getRole());
        if (!transition.getRole().equals("ADMIN") && !isSubordinate) return badRequest();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserAction> actions = userActionRepository.findAllByCreatorId(userId, pageable);
        Page<ActionResponse> responsePage = actionMapper.toDto(actions);
        return success(responsePage);
    }
}
