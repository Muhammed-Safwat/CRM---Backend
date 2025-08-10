package com.gws.crm.core.notification.services;

import com.gws.crm.core.notification.entities.CrmNotification;

import java.io.IOException;

public interface EmailTemplateService {

    String generateHtmlTemplate(CrmNotification crmNotification) throws IOException;

}
