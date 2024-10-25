package com.gws.crm.core.employee.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReplayResponse {
    private String reply;
    private LocalDateTime createdAt;
    private String name;
    private String jobTile;
    private String image;
}
