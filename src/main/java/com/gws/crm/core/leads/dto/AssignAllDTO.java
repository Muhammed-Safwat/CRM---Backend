package com.gws.crm.core.leads.dto;


import com.google.firebase.database.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AssignAllDTO {
    @NotNull
    private List<Long> leadsIds;
    private long salesId;

}