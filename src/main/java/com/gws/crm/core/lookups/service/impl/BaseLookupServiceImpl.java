package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.actions.repository.repository.EmployeeRepository;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.BaseLookup;
import com.gws.crm.core.lookups.repository.BaseLookupRepository;
import com.gws.crm.core.lookups.service.BaseLookupService;
import com.gws.crm.core.lookups.spcification.LookupSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseLookupServiceImpl<T extends BaseLookup, D extends LookupDTO> implements BaseLookupService<T, D> {

    private final BaseLookupRepository<T> repository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String keyword, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        long id = transition.getUserId();
        if ("USER".equals(transition.getRole())) {
            Employee employee = employeeRepository.findById(transition.getUserId())
                    .orElseThrow(NotFoundResourceException::new);
            id = employee.getAdmin().getId();
        }

        Specification<T> specification = LookupSpecification.filter(keyword, id, transition);

        Page<T> lookupPage = repository.findAll(specification, pageable);

        log.info("keyword ====== {}", lookupPage);

        return success(lookupPage);
    }

    @Override
    public ResponseEntity<?> getAll(Transition transition) {
        long id = transition.getUserId();
        if ("USER".equals(transition.getRole())) {
            Employee employee = employeeRepository.findById(transition.getUserId())
                    .orElseThrow(NotFoundResourceException::new);
            id = employee.getAdmin().getId();
        }
        List<T> lookupList = repository.findAllByAdminId(id);
        log.info(lookupList.toString());
        return success(lookupList);
    }

    @Override
    public ResponseEntity<?> getById(long id, Transition transition) {
        T lookup = repository.findByIdAndAdminId(id, transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        return success(lookup);
    }

    @Override
    public ResponseEntity<?> create(D dto, Transition transition) {
        T entity = mapDtoToEntity(dto, transition);
        entity.setAdmin(adminRepository.getReferenceById(transition.getUserId()));
        T savedEntity = repository.save(entity);
        return success(mapEntityToDto(savedEntity));
    }

    @Override
    public ResponseEntity<?> update(D dto, Transition transition) {
        T entity = repository.findByIdAndAdminId(dto.getId(), transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        updateEntityFromDto(entity, dto);
        T savedEntity = repository.save(entity);
        return success(mapEntityToDto(savedEntity));
    }

    @Override
    public ResponseEntity<?> delete(long id, Transition transition) {
        repository.deleteByIdAndAdminId(id, transition.getUserId());
        return success("Deleted successfully");
    }

    protected abstract T mapDtoToEntity(D dto, Transition transition);

    protected abstract D mapEntityToDto(T entity);

    protected abstract void updateEntityFromDto(T entity, D dto);

}