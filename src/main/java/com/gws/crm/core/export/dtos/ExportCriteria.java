package com.gws.crm.core.export.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class ExportCriteria {
    private String type;
    private String referenceType;
    private String status;
    private List<LocalDateTime> createdAt;
    private String keyword;
    private int page;
    private int size;
}

