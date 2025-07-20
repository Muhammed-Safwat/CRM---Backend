package com.gws.crm.core.notes.dtos;

import lombok.Data;

@Data
public class NoteResponseDTO {
    private Long id;
    private String targetType;
    private String description;
    private String title;
    private Long targetId;
    private String createdAt;
    private String updatedAt;
    private boolean favorite;
    private boolean archived;
    private Long creatorId;
    private String label ;
}
