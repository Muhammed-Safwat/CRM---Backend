package com.gws.crm.core.notification.services.imp;

import com.gws.crm.core.notification.builder.EmailPlaceholdersFactoryImp;
import com.gws.crm.core.notification.builder.EmailTemplateLoaderImp;
import com.gws.crm.core.notification.dtos.BaseTemplateData;
import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.services.EmailTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class EmailTemplateServiceImp implements EmailTemplateService {

    private final EmailTemplateLoaderImp emailTemplateLoaderImp;
    private final EmailPlaceholdersFactoryImp emailPlaceholdersFactoryImp;

    @Override
    public String generateHtmlTemplate(CrmNotification crmNotification) throws IOException {
        BaseTemplateData baseTemplateData = emailPlaceholdersFactoryImp.getPlaceholder(crmNotification);
        return emailTemplateLoaderImp.buildTemplate(crmNotification.getType(), baseTemplateData);
    }


}
