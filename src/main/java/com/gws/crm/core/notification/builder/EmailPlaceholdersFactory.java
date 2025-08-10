package com.gws.crm.core.notification.builder;

import com.gws.crm.core.notification.dtos.BaseTemplateData;
import com.gws.crm.core.notification.entities.CrmNotification;

public interface EmailPlaceholdersFactory {

    BaseTemplateData getPlaceholder(CrmNotification crmNotification);
}
