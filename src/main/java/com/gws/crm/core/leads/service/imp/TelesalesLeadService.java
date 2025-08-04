package com.gws.crm.core.leads.service.imp;

import com.gws.crm.common.entities.ExcelFile;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.service.ExcelSheetService;
import com.gws.crm.core.notification.publisher.TeleSalesLeadNotificationEventPublisher;
import com.gws.crm.core.actions.event.telesales.*;
import com.gws.crm.core.actions.service.imp.TeleSalesLeadActionServiceImp;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.entity.TeleSalesLead;
import com.gws.crm.core.leads.factory.TeleSalesLeadFactory;
import com.gws.crm.core.leads.mapper.TeleSalesLeadMapper;
import com.gws.crm.core.leads.repository.TeleSalesLeadRepository;
import com.gws.crm.core.notification.builder.LeadNotificationEventBuilder;
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
public class TelesalesLeadService extends SalesLeadServiceImp<TeleSalesLead, AddLeadDTO>   {

    private final TeleSalesLeadRepository leadRepository;
    private final TeleSalesLeadMapper leadMapper;
    private final ExcelSheetService excelSheetService;
    private final TeleSalesLeadFactory teleSalesLeadFactory;
    private final TeleSalesPhoneValidator teleSalesPhoneValidator;
    private final ApplicationEventPublisher eventPublisher;
    private final LeadNotificationEventBuilder<TeleSalesLead> leadNotificationEventBuilder;
    private final TeleSalesLeadNotificationEventPublisher teleSalesLeadNotificationEventPublisher;
    protected TelesalesLeadService(TeleSalesLeadRepository leadRepository, EmployeeRepository employeeRepository,
                                   TeleSalesLeadMapper leadMapper,
                                   ExcelSheetService excelSheetService,
                                   TeleSalesLeadActionServiceImp leadActionService,
                                   TeleSalesLeadFactory teleSalesLeadFactory, TeleSalesPhoneValidator teleSalesPhoneValidator, ApplicationEventPublisher eventPublisher, LeadNotificationEventBuilder<TeleSalesLead> leadNotificationEventBuilder, TeleSalesLeadNotificationEventPublisher teleSalesLeadNotificationEventPublisher) {
        super(leadRepository, employeeRepository);
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
        this.excelSheetService = excelSheetService;
        this.teleSalesLeadFactory = teleSalesLeadFactory;
        this.teleSalesPhoneValidator = teleSalesPhoneValidator;
        this.eventPublisher = eventPublisher;
        this.leadNotificationEventBuilder = leadNotificationEventBuilder;
        this.teleSalesLeadNotificationEventPublisher = teleSalesLeadNotificationEventPublisher;
    }

    @Override
    protected TeleSalesLead mapDtoToEntity(AddLeadDTO leadDTO, Transition transition) {
        return teleSalesLeadFactory.mapDtoToEntity(leadDTO,transition);
    }

    @Override
    protected LeadResponse mapEntityToDto(TeleSalesLead entity) {
        return leadMapper.toDTO(entity);
    }

    @Override
    protected Page<LeadResponse> mapEntityToDto(Page<TeleSalesLead> entityPage) {
        return leadMapper.toDTOPage(entityPage);
    }

    @Override
    protected void updateEntityFromDto(TeleSalesLead entity, AddLeadDTO leadDTO, Transition transition) {
      teleSalesLeadFactory.updateEntityFromDto(entity,leadDTO,transition);
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
        List<TeleSalesLead> leadList = createLeadsList(leads, transition);
        leadRepository.saveAll(leadList);
        return success("Lead Imported Successfully");
    }

    @Override
    public ResponseEntity<?> isPhoneExist(String phone, Transition transition) {
        return teleSalesPhoneValidator.isPhoneExist(phone,transition);

    }

    @Override
    public ResponseEntity<?> isPhoneExist(List<String> phones, Transition transition) {
        return teleSalesPhoneValidator.isPhoneExist(phones,transition);
    }


    private List<TeleSalesLead> createLeadsList(List<ImportLeadDTO> importLeadDTOS, Transition transition) {
         return teleSalesLeadFactory.createLeadsList(importLeadDTOS,transition);
    }

    @Override
    public void publishCreateLeadEvent(TeleSalesLead lead, Transition transition) {
        eventPublisher.publishEvent(new TeleSalesLeadCreatedEvent(lead, transition));
        teleSalesLeadNotificationEventPublisher.publishCreateLeadEvent(lead,transition);
    }

    @Override
    public void publishDeleteLeadEvent(TeleSalesLead lead, Transition transition) {
        eventPublisher.publishEvent(new TeleSalesLeadDeletedEvent(lead, transition));
       teleSalesLeadNotificationEventPublisher.publishDeleteLeadEvent(lead,transition);
    }

    @Override
    public void publishEditLeadEvent(TeleSalesLead lead, Transition transition) {
        eventPublisher.publishEvent(new TeleSalesLeadEditedEvent(lead, transition));
        teleSalesLeadNotificationEventPublisher.publishEditLeadEvent(lead,transition);
    }

    @Override
    public void publishRestoreLeadEvent(TeleSalesLead lead, Transition transition) {
        eventPublisher.publishEvent(new TeleSalesLeadRestoredEvent(lead, transition));
        teleSalesLeadNotificationEventPublisher.publishRestoreLeadEvent(lead,transition);
    }

    @Override
    public void publishAssignLeadEvent(TeleSalesLead lead, Employee lastSales, Transition transition) {
        eventPublisher.publishEvent(new TeleSalesLeadAssignedEvent(lead, transition));
        teleSalesLeadNotificationEventPublisher.publishAssignLeadEvent(lead,lastSales,transition);
    }

    @Override
    public void publishViewLeadEvent(TeleSalesLead lead, Transition transition) {
        if(!lead.isReviewedBySales() && lead.getSalesRep().getId() == transition.getUserId()){
            eventPublisher.publishEvent(new TeleSalesLeadViewedEvent(lead, transition));
            teleSalesLeadNotificationEventPublisher.publishViewLeadEvent(lead,transition);
        }
      }

    @Override
    public void publishDelayLeadEvent(TeleSalesLead lead, Transition transition) {
        eventPublisher.publishEvent(new TeleSalesLeadDelayedEvent(lead, transition));
        teleSalesLeadNotificationEventPublisher.publishDelayLeadEvent(lead,transition);
    }



}
