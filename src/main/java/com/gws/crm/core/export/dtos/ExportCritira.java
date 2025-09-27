package com.gws.crm.core.export.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class ExportCritira {
    private String type;
    private String referenceType;
    private String status;
    private LocalDate createdAt;
    private List<Long> exporterIds;
    private String keyword;
    private int page ;
    private int size ;
}
