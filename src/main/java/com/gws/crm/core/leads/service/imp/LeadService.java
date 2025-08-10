package com.gws.crm.core.leads.service.imp;

import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.common.service.PhoneValidationService;
import com.gws.crm.core.actions.event.lead.*;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.factory.LeadFactory;
import com.gws.crm.core.leads.mapper.LeadMapper;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.notification.publisher.LeadNotificationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;

@Service
@Slf4j
public class LeadService extends SalesLeadServiceImp<Lead, AddLeadDTO> {

    private final LeadRepository leadRepository;
    private final ExcelSheetService excelSheetService;
    private final LeadFactory leadFactory;
    private final PhoneValidationService phoneValidationService;
    private final LeadMapper leadMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final LeadNotificationEventPublisher leadNotificationEventPublisher;


    protected LeadService(LeadRepository leadRepository,
                          EmployeeRepository employeeRepository, ExcelSheetService excelSheetService,
                          LeadFactory leadFactory,
                          PhoneValidationService phoneValidationService, LeadMapper leadMapper,
                          ApplicationEventPublisher eventPublisher,
                          LeadNotificationEventPublisher leadNotificationEventPublisher) {
        super(leadRepository, employeeRepository);
        this.leadRepository = leadRepository;
        this.excelSheetService = excelSheetService;
        this.leadFactory = leadFactory;
        this.phoneValidationService = phoneValidationService;
        this.leadMapper = leadMapper;
        this.eventPublisher = eventPublisher;
        this.leadNotificationEventPublisher = leadNotificationEventPublisher;
    }

    @Override
    public ResponseEntity<?> generateExcel(Transition transition) {
        ExcelFile excelFile = ExcelFile.builder()
                .header(generateHeader(AddLeadDTO.class))
                .dropdowns(excelSheetService.generateLeadExcelSheetMap(transition)).build();
        return success(excelFile);
    }

    @Override
    public ResponseEntity<?> importLead(List<ImportLeadDTO> leads, Transition transition) {
        List<Lead> leadList = createLeadsList(leads, transition);
        List<Lead> savedLeads = leadRepository.saveAll(leadList);
        // leadActionService.setImportLeads(savedLeads,transition);
        return success("Lead Imported Successfully");
    }

    @Override
    public ResponseEntity<?> isPhoneExist(List<String> phones, Transition transition) {
        return phoneValidationService.isPhoneExist(phones, transition);
    }

    @Override
    public ResponseEntity<?> isPhoneExist(String phone, Transition transition) {
        return phoneValidationService.isPhoneExist(phone, transition);
    }


    private List<Lead> createLeadsList(List<ImportLeadDTO> importLeadDTOS, Transition transition) {
        return leadFactory.createLeadsList(importLeadDTOS, transition);
    }

    @Override
    protected Lead mapDtoToEntity(AddLeadDTO leadDTO, Transition transition) {
        return leadFactory.mapDtoToEntity(leadDTO, transition);
    }

    @Override
    protected LeadResponse mapEntityToDto(Lead entity) {
        return leadMapper.toDTO(entity);
    }

    @Override
    protected Page<LeadResponse> mapEntityToDto(Page<Lead> entityPage) {
        return leadMapper.toDTOPage(entityPage);
    }

    @Override
    protected void updateEntityFromDto(Lead existingLead, AddLeadDTO leadDTO, Transition transition) {
        leadFactory.updateEntityFromDto(existingLead, leadDTO, transition);
    }

    @Override
    public void publishCreateLeadEvent(Lead lead, Transition transition) {
        eventPublisher.publishEvent(new LeadCreatedEvent(lead, transition));
        // create lead but not admin
        leadNotificationEventPublisher.publishCreateLeadEvent(lead, transition);
    }

    @Override
    public void publishDeleteLeadEvent(Lead lead, Transition transition) {
        eventPublisher.publishEvent(new LeadDeletedEvent(lead, transition));
        // admin delete lead
        leadNotificationEventPublisher.publishDeleteLeadEvent(lead, transition);
    }

    @Override
    public void publishEditLeadEvent(Lead lead, Transition transition) {
        eventPublisher.publishEvent(new LeadEditedEvent(lead, transition));
        // admin update lead
        leadNotificationEventPublisher.publishEditLeadEvent(lead, transition);
    }

    @Override
    public void publishRestoreLeadEvent(Lead lead, Transition transition) {
        eventPublisher.publishEvent(new LeadRestoredEvent(lead, transition));
        // admin restore lead
        leadNotificationEventPublisher.publishRestoreLeadEvent(lead, transition);
    }

    @Override
    public void publishAssignLeadEvent(Lead lead, Employee lastSales, Transition transition) {
        eventPublisher.publishEvent(new LeadAssignedEvent(lead, transition));
        // for new sales
        leadNotificationEventPublisher.publishAssignLeadEvent(lead, lastSales, transition);
    }

    @Override
    public void publishViewLeadEvent(Lead lead, Transition transition) {
        // send this event once (first time reviewed by sales)
        leadNotificationEventPublisher.publishViewLeadEvent(lead, transition);
    }

    @Override
    public void publishDelayLeadEvent(Lead lead, Transition transition) {
        eventPublisher.publishEvent(new LeadDelayedEvent(lead, transition));
        // when delayed event occur send notification to sales admin
        leadNotificationEventPublisher.publishDelayLeadEvent(lead, transition);
    }
}
