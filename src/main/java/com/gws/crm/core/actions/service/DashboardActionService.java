package com.gws.crm.core.actions.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.actions.dtos.ActionCriteria;
import com.gws.crm.core.actions.dtos.ActionResponse;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.actions.mapper.ActionMapper;
import com.gws.crm.core.actions.repository.UserActionRepository;
import com.gws.crm.core.actions.specification.ActionSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@AllArgsConstructor
public class DashboardActionService {

    private final ActionMapper actionMapper;
    private final UserActionRepository userActionRepository;

    public ResponseEntity<?> getActions(ActionCriteria criteria, Transition transition) {
        Sort sort = createSort(criteria);
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Specification<UserAction> spec = ActionSpecification.filter(criteria, transition);

        Page<UserAction> actions = userActionRepository.findAll(spec, pageable);
        Page<ActionResponse> responsePage = actionMapper.toDto(actions);

        return success(responsePage);
    }

    private Sort createSort(ActionCriteria criteria) {
        String sortBy = criteria.getSortBy() != null ? criteria.getSortBy() : "createdAt";
        String sortDirection = criteria.getSortDirection() != null ? criteria.getSortDirection() : "DESC";

        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return Sort.by(direction, sortBy);
    }
}
