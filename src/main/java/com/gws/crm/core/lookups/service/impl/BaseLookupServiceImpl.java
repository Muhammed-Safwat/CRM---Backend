package com.gws.crm.core.lookups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.employee.repository.EmployeeRepository;
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
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<T>  spec  = LookupSpecification.filter(keyword,transition.getUserId(),transition);
        Page<T> list = repository.findAll(spec ,pageable);
        Page<D> dtoPage = list.map(this::mapEntityToDto);
        return ResponseEntity.ok(dtoPage);
    }

    @Override
    public ResponseEntity<?> getAll(Transition transition) {
        long adminId = resolveAdminId(transition);
        List<T> lookupList = repository.findAllByAdminIdAndDeletedFalse(adminId);
        return success(lookupList);
    }

    @Override
    public ResponseEntity<?> getById(long id, Transition transition) {
        long adminId = resolveAdminId(transition);
        T lookup = repository.findByIdAndAdminId(id, adminId)
                .orElseThrow(NotFoundResourceException::new);
        return success(lookup);
    }

    @Override
    public ResponseEntity<?> create(D dto, Transition transition) {
        T entity = mapDtoToEntity(dto, transition);
        entity.setAdmin(adminRepository.getReferenceById(resolveAdminId(transition)));
        T savedEntity = repository.save(entity);
        return success(mapEntityToDto(savedEntity));
    }

    @Override
    public ResponseEntity<?> update(D dto, Transition transition) {
        long adminId = resolveAdminId(transition);
        T entity = repository.findByIdAndAdminId(dto.getId(), adminId)
                .orElseThrow(NotFoundResourceException::new);

        updateEntityFromDto(entity, dto);
        T savedEntity = repository.save(entity);
        return success(mapEntityToDto(savedEntity));
    }

    @Override
    public ResponseEntity<?> delete(long id, Transition transition) {
        long adminId = resolveAdminId(transition);
        T entity = repository.findByIdAndAdminId(id, adminId)
                .orElseThrow(NotFoundResourceException::new);

        entity.setDeleted(true);
        repository.save(entity);

        return success("Deleted successfully");
    }

    private long resolveAdminId(Transition transition) {
        if ("USER".equals(transition.getRole())) {
            Employee employee = employeeRepository.findById(transition.getUserId())
                    .orElseThrow(NotFoundResourceException::new);
            return employee.getAdmin().getId();
        }
        return transition.getUserId();
    }

    protected abstract T mapDtoToEntity(D dto, Transition transition);

    protected abstract D mapEntityToDto(T entity);

    protected abstract void updateEntityFromDto(T entity, D dto);
}
