package com.gws.crm.core.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AssignSalesTemplateData extends BaseTemplateData {
    private LocalDateTime assignDate;

    @Override
    protected void addSpecificVariables(Map<String, String> variables) {

    }

}
