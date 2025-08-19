package com.gws.crm.core.notification.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class BaseTemplateData implements TemplateData {
    protected String recipientName;
    protected String crmLink;
    protected String companyName;
    protected String companyAddress;
    private String leadName;
    private String leadEmail;
    private String leadPhone;
    private String leadProject;

    @Override
    public Map<String, String> getTemplateVariables() {
        Map<String, String> variables = new HashMap<>();
        variables.put("RECIPIENT_NAME", recipientName);
        variables.put("COMPANY_NAME", companyName);
        variables.put("CRM_LINK", crmLink);
        variables.put("COMPANY_ADDRESS", companyAddress);
        variables.put("LEAD_NAME", leadName);
        variables.put("LEAD_EMAIL", leadEmail);
        variables.put("LEAD_PHONE", leadPhone);
        variables.put("LEAD_PROJECT", leadProject);
        addSpecificVariables(variables);
        return variables;
    }

    protected abstract void addSpecificVariables(Map<String, String> variables);
}