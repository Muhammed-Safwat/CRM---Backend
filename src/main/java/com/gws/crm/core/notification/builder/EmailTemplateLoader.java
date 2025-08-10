package com.gws.crm.core.notification.builder;

import com.gws.crm.core.notification.dtos.BaseTemplateData;
import com.gws.crm.core.notification.entities.NotificationType;

import java.io.IOException;

public interface EmailTemplateLoader {
    String buildTemplate(NotificationType notificationType, BaseTemplateData baseTemplateData) throws IOException;
}
