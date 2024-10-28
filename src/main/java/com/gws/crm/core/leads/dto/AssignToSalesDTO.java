package com.gws.crm.core.leads.dto;


import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class AssignToSalesDTO {
    private ArrayList<Long> leadsIds;
    private long salesId ;
}
