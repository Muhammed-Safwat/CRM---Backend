package com.gws.crm.core.notes.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NoteCriteria {
    private Long noteTypeId;
    private String noteType;
    private Long targetId;
    private LocalDate createdAt;
    private String keyword;
    private String label;
    private Boolean favorite;
    private Boolean archived;
    private int page;
    private int size;
}