package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.*;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.repository.GenericSalesLeadRepository;
import com.gws.crm.core.leads.service.SalesLeadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.gws.crm.common.handler.ApiResponseHandler.*;
import static com.gws.crm.core.leads.specification.SalesLeadSpecification.filter;

@Slf4j
@Service
@Transactional
public abstract class SalesLeadServiceImp<T extends SalesLead, D extends AddLeadDTO> implements SalesLeadService<T,
        D> {

    private final GenericSalesLeadRepository<T> repository;
    private final EmployeeRepository employeeRepository;

    protected SalesLeadServiceImp(GenericSalesLeadRepository<T> repository,
                                  EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ResponseEntity<?> getLeadDetails(long leadId, Transition transition) {
        T lead = repository.findById(leadId)
                .orElseThrow(NotFoundResourceException::new);
        LeadResponse leadResponse = mapEntityToDto(lead);
        // actionServiceImp.setSalesViewLeadAction(lead, transition);
        return success(leadResponse);
    }

    @Override
    public ResponseEntity<?> addLead(D leadDTO, Transition transition) {
        T entity = mapDtoToEntity(leadDTO, transition);
        T savedLead = repository.save(entity);
        LeadResponse leadResponse = mapEntityToDto(savedLead);
        publishCreateLeadEvent(savedLead, transition);
        return created(leadResponse);
    }

    @Override
    @Transactional
    @Modifying
    public ResponseEntity<?> updateLead(D leadDTO, Transition transition) {
        T existingEntity = repository.findById(leadDTO.getId())
                .orElseThrow(NotFoundResourceException::new);
        updateEntityFromDto(existingEntity, leadDTO, transition);
        T updatedEntity = repository.save(existingEntity);
        // actionServiceImp.setLeadEditionAction(updatedEntity, transition);
        LeadResponse leadResponse = mapEntityToDto(updatedEntity);
        publishCreateLeadEvent(updatedEntity, transition);
        return ResponseEntity.ok(leadResponse);
    }

    @Override
    public ResponseEntity<?> deleteLead(long leadId, Transition transition) {
        T lead = repository.findById(leadId).orElseThrow(NotFoundResourceException::new);
        repository.deleteLead(leadId);
        publishDeleteLeadEvent(lead, transition);
        return success("Lead Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> toggleArchive(long leadId, Transition transition) {
        T lead = repository.findById(leadId)
                .orElseThrow(NotFoundResourceException::new);

        boolean newArchiveStatus = !lead.isArchive();

        repository.toggleArchive(leadId, newArchiveStatus);

        String message = newArchiveStatus ? "Lead Archived Successfully" : "Lead Unarchived Successfully";

        return success(message);
    }

    @Override
    public ResponseEntity<?> getLeads(SalesLeadCriteria salesLeadCriteria, Transition transition) {
        if (transition.getRole().equals("USER")) {
            Employee employee =
                    employeeRepository.findById(transition.getUserId())
                            .orElseThrow(NotFoundResourceException::new);
            salesLeadCriteria.setSubordinates(employee.getSubordinates()
                    .stream().map(User::getId).toList());
            log.info("********************** %%%%%%%%%%%%%%%%% **********************");
            log.info(salesLeadCriteria.getSubordinates().toString());
        }
        Specification<T> leadSpecification = filter(salesLeadCriteria, transition);
        Pageable pageable = PageRequest.of(salesLeadCriteria.getPage(), salesLeadCriteria.getSize());
        Page<T> leadPage = repository.findAll(leadSpecification, pageable);
        Page<LeadResponse> leadResponses = mapEntityToDto(leadPage);
        return success(leadResponses);
    }

    @Override
    public ResponseEntity<?> restoreLead(Long leadId, Transition transition) {
        T lead = repository.findById(leadId).orElseThrow(NotFoundResourceException::new);
        repository.restoreLead(leadId);
        // actionServiceImp.setLeadRestoreAction(lead, transition);
        publishRestoreLeadEvent(lead, transition);
        return success("Lead restored Successfully");
    }

    @Override
    public ResponseEntity<?> assignSalesToLead(AssignDTO assignDTO, Transition transition) {
        T lead = repository.findById(assignDTO.getLeadId())
                .orElseThrow(NotFoundResourceException::new);
        log.info("Lead Id ***************");
        log.info("{} =======", lead);
        Employee lastSalesRep = null;
        if (lead.getSalesRep() != null &&
                Objects.equals(lead.getSalesRep().getId(), assignDTO.getSalesId())) {
            return badRequest();
        } else if (lead.getSalesRep() != null && !Objects.equals(lead.getSalesRep().getId(), assignDTO.getSalesId())) {
            lastSalesRep = lead.getSalesRep();
        }

        Employee employee = employeeRepository.findById(assignDTO.getSalesId())
                .orElseThrow(NotFoundResourceException::new);
        lead.setSalesRep(employee);
        lead.setAssignAt(LocalDateTime.now());
        // actionServiceImp.setAssignAction(lead, transition);
        AssignResponse response = AssignResponse.builder()
                .salesName(employee.getName())
                .jobTitle(employee.getJobName())
                .assignAt(LocalDateTime.now())
                .build();
        publishAssignLeadEvent(lead, lastSalesRep, transition);
        return success(response);
    }


    protected abstract T mapDtoToEntity(D dto, Transition transition);

    protected abstract LeadResponse mapEntityToDto(T entity);

    protected abstract Page<LeadResponse> mapEntityToDto(Page<T> entityPage);

    protected abstract void updateEntityFromDto(T entity, D dto, Transition transition);

    public void publishCreateLeadEvent(T lead, Transition transition) {
    }

    public void publishDeleteLeadEvent(T lead, Transition transition) {
    }

    public void publishEditLeadEvent(T lead, Transition transition) {
    }

    public void publishRestoreLeadEvent(T lead, Transition transition) {
    }

    public void publishAssignLeadEvent(T lead, Employee lastSales, Transition transition) {
    }

    public void publishViewLeadEvent(T lead, Transition transition) {
    }

    public void publishDelayLeadEvent(T lead, Transition transition) {
    }
}
