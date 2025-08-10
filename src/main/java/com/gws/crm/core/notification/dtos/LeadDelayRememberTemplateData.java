package com.gws.crm.core.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class LeadDelayRememberTemplateData extends BaseTemplateData {

    private String delayDuration;
    private String recipientName;

    @Override
    protected void addSpecificVariables(Map<String, String> variables) {
        variables.put("DELAY_DURATION", delayDuration);
        variables.put("RECIPIENT_NAME", recipientName);
    }
}
