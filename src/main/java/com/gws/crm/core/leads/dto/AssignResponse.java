package com.gws.crm.core.leads.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AssignResponse {
    private LocalDateTime assignAt;
    private String salesName;
    private String jobTitle;
}
