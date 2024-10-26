package com.gws.crm.core.employee.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentResponse {
    private long id;
    private String comment;
    private LocalDateTime createdAt;
    private String name;
    private String jobTile;
    private String image;
    private List<ReplayResponse> replies;
}
