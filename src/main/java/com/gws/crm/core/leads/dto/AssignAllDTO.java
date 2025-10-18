package com.gws.crm.core.leads.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AssignAllDTO {
    private List<Long> leadIds;
    private long salesId;

}
