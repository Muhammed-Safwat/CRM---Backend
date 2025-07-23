package com.gws.crm.core.notes.dtos;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNoteIntoLeadDto {
    private String title;
    private String description;
    private Long targetId;
}
