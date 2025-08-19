package com.gws.crm.core.notification.builder;

import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import com.gws.crm.core.notification.dtos.*;
import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.entities.NotificationType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmailPlaceholdersFactoryImp implements EmailPlaceholdersFactory {

    private final BaseLeadRepository baseLeadRepository;

    @Override
    public BaseTemplateData getPlaceholder(CrmNotification crmNotification) {
        NotificationType notificationType = crmNotification.getType();
        if (notificationType == null || notificationType.getCode() == null) {
            return null;
        }

        return switch (notificationType.getCode()) {
            case "ASSIGN_SALES_TO_LEAD" -> assignSalesToLeadPlaceholders(crmNotification);
            case "LEAD_RETURNED_TO_ADMIN" -> leadReturnedToAdminPlaceholders(crmNotification);
            case "LEAD_COMMENT_ADDED" -> leadCommentAddedPlaceholders(crmNotification);
            case "LEAD_DELAYED" -> leadDelayedPlaceholders(crmNotification);
            case "LEAD_TRANSFER" -> leadTransferPlaceholders(crmNotification);
            case "LEAD_DELAY_REMEMBER" -> leadDelayRememberPlaceholders(crmNotification);
            default -> null;
        };
    }

    private BaseTemplateData assignSalesToLeadPlaceholders(CrmNotification notification) {
        BaseLead lead = baseLeadRepository.findById(notification.getReferenceId())
                .orElseThrow(NotFoundResourceException::new);
        AssignSalesTemplateData assignSalesTemplateData = new AssignSalesTemplateData();
        setBasicInfo(notification, lead, assignSalesTemplateData);
        assignSalesTemplateData.setCrmLink("");
        return assignSalesTemplateData;
    }

    private BaseTemplateData leadReturnedToAdminPlaceholders(CrmNotification notification) {
        BaseLead lead = baseLeadRepository.findById(notification.getReferenceId())
                .orElseThrow(NotFoundResourceException::new);
        LeadReturnedTemplateData leadReturnedTemplateData = new LeadReturnedTemplateData();
        setBasicInfo(notification, lead, leadReturnedTemplateData);
        leadReturnedTemplateData.setCrmLink("");
        return leadReturnedTemplateData;
    }

    private BaseTemplateData leadCommentAddedPlaceholders(CrmNotification notification) {
        BaseLead lead = baseLeadRepository.findById(notification.getReferenceId())
                .orElseThrow(NotFoundResourceException::new);
        LeadCommentTemplateData leadCommentTemplateData = new LeadCommentTemplateData();
        setBasicInfo(notification, lead, leadCommentTemplateData);
        leadCommentTemplateData.setCrmLink("");
        return leadCommentTemplateData;
    }

    private BaseTemplateData leadDelayedPlaceholders(CrmNotification notification) {
        BaseLead lead = baseLeadRepository.findById(notification.getReferenceId())
                .orElseThrow(NotFoundResourceException::new);
        LeadDelayedTemplateData leadDelayedTemplateData = new LeadDelayedTemplateData();
        setBasicInfo(notification, lead, leadDelayedTemplateData);
        leadDelayedTemplateData.setCrmLink("");
        return leadDelayedTemplateData;
    }

    private BaseTemplateData leadTransferPlaceholders(CrmNotification notification) {
        BaseLead lead = baseLeadRepository.findById(notification.getReferenceId())
                .orElseThrow(NotFoundResourceException::new);
        LeadTransferTemplateData leadTransferTemplateData = new LeadTransferTemplateData();
        setBasicInfo(notification, lead, leadTransferTemplateData);
        leadTransferTemplateData.setCrmLink("");
        return leadTransferTemplateData;
    }

    private BaseTemplateData leadDelayRememberPlaceholders(CrmNotification notification) {
        BaseLead lead = baseLeadRepository.findById(notification.getReferenceId())
                .orElseThrow(NotFoundResourceException::new);
        LeadDelayRememberTemplateData leadDelayRememberTemplateData = new LeadDelayRememberTemplateData();
        setBasicInfo(notification, lead, leadDelayRememberTemplateData);
        leadDelayRememberTemplateData.setCrmLink("");
        return leadDelayRememberTemplateData;
    }

    private void setBasicInfo(CrmNotification notification, BaseLead lead, BaseTemplateData baseTemplateData) {
        baseTemplateData.setLeadName(lead.getName());
        baseTemplateData.setLeadEmail(lead.getEmail());
        baseTemplateData.setLeadPhone(lead.getPhoneNumbers() == null ? "" :
                lead.getPhoneNumbers().stream().map(PhoneNumber::getNumber).collect(Collectors.joining(", ")));
        baseTemplateData.setLeadProject(lead.getProject().getName());
        baseTemplateData.setRecipientName(notification.getRecipientName());
        baseTemplateData.setCompanyName("");
        baseTemplateData.setCompanyAddress("");
    }


}
