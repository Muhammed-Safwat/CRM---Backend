package com.gws.crm.core.notification.dtos;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LeadReturnedTemplateData extends BaseTemplateData {

    private String returnedBy;
    private String returnDate;
    private String returnReason;
    private String daysWithSales;
    private String leadScore;

    @Override
    protected void addSpecificVariables(Map<String, String> variables) {
        variables.put("RETURNED_BY", returnedBy);
        variables.put("RETURN_DATE", returnDate);
        variables.put("RETURN_REASON", returnReason);
        variables.put("DAYS_WITH_SALES", daysWithSales);
        variables.put("LEAD_SCORE", leadScore);
    }
}