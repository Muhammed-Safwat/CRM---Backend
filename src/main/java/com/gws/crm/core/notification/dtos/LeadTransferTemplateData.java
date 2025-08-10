package com.gws.crm.core.notification.dtos;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LeadTransferTemplateData extends BaseTemplateData {

    private String fromUser;
    private String toUser;
    private String transferDate;
    private String transferMessage;

    @Override
    protected void addSpecificVariables(Map<String, String> variables) {
        variables.put("FROM_USER", fromUser);
        variables.put("TO_USER", toUser);
        variables.put("TRANSFER_DATE", transferDate);
        variables.put("TRANSFER_MESSAGE", transferMessage);
    }
}
