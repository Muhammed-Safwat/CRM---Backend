package com.gws.crm.core.notification.builder;

import com.gws.crm.core.notification.dtos.BaseTemplateData;
import com.gws.crm.core.notification.entities.NotificationType;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@AllArgsConstructor
public class EmailTemplateLoaderImp implements EmailTemplateLoader {

    private final ResourceLoader resourceLoader;

    public String buildTemplate(NotificationType notificationType, BaseTemplateData baseTemplateData) throws IOException {
        try {
            Map<String, String> placeholders = baseTemplateData.getTemplateVariables();
            Resource resource = resourceLoader.getResource("classpath:templates/emails/" + notificationType.getCode() +
                    ".html");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return content;
        } catch (IOException e) {
            throw new RuntimeException("Error loading email template", e);
        }
    }

}
