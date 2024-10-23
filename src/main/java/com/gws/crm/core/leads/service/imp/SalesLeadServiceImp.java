package com.gws.crm.core.leads.service.imp;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.dto.SalesLeadCriteria;
import com.gws.crm.core.leads.entity.SalesLead;
import com.gws.crm.core.leads.repository.SalesLeadRepository;
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

import static com.gws.crm.common.handler.ApiResponseHandler.created;
import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.core.leads.specification.SalesLeadSpecification.filter;

@Slf4j
@Service
public abstract class SalesLeadServiceImp<T extends SalesLead, D extends AddLeadDTO> implements SalesLeadService<T, D> {

    private final SalesLeadRepository<T> repository;

    protected SalesLeadServiceImp(SalesLeadRepository<T> repository) {
        this.repository = repository;
    }

    /*
        private final LeadStatusRepository leadStatusRepository;
        private final InvestmentGoalRepository investmentGoalRepository;
        private final CommunicateWayRepository communicateWayRepository;
        private final CancelReasonsRepository cancelReasonsRepository;
        private final EmployeeRepository employeeRepository;
        private final ChannelRepository channelRepository;
        private final ProjectRepository projectRepository;
        private final UserRepository userRepository;
        private final PhoneNumberMapper phoneNumberMapper;
        private final ExcelSheetService excelSheetService;
        private final BrokerRepository brokerRepository;
        private final PhoneNumberRepository phoneNumberRepository;
    */
    @Override
    public ResponseEntity<?> getLeadDetails(long leadId, Transition transition) {
        T lead = repository.findById(leadId)
                .orElseThrow(NotFoundResourceException::new);
        // LeadResponse leadResponse  = leadMapper.toDTO(lead);
        LeadResponse leadResponse = mapEntityToDto(lead);
        return success(leadResponse);
    }


    @Override
    public ResponseEntity<?> addLead(D leadDTO, Transition transition) {
        T entity = mapDtoToEntity(leadDTO, transition);
        T savedLead = repository.save(entity);
        LeadResponse leadResponse = mapEntityToDto(savedLead);
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
        LeadResponse leadResponse = mapEntityToDto(updatedEntity);
        return ResponseEntity.ok(leadResponse);
    }

    @Override
    public ResponseEntity<?> deleteLead(long leadId, Transition transition) {
        repository.deleteLead(leadId);
        return success("Lead Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> getLeads(SalesLeadCriteria salesLeadCriteria, Transition transition) {
        Specification<T> leadSpecification = filter(salesLeadCriteria, transition);
        Pageable pageable = PageRequest.of(salesLeadCriteria.getPage(), salesLeadCriteria.getSize());
        Page<T> leadPage = repository.findAll(leadSpecification, pageable);
        Page<LeadResponse> leadResponses = mapEntityToDto(leadPage);
        return success(leadResponses);
    }

    @Override
    public ResponseEntity<?> restoreLead(Long leadId, Transition transition) {
        repository.restoreLead(leadId);
        return success("Lead restored Successfully");
    }

    protected abstract T mapDtoToEntity(D dto, Transition transition);

    protected abstract LeadResponse mapEntityToDto(T entity);

    protected abstract Page<LeadResponse> mapEntityToDto(Page<T> entityPage);

    protected abstract void updateEntityFromDto(T entity, D dto, Transition transition);

}
