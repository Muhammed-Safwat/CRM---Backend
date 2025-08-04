package com.gws.crm.core.notes.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNoteDTO {
    private String title;
    private String description;
    private String label;
    private String type;
    private Long targetId;
}
