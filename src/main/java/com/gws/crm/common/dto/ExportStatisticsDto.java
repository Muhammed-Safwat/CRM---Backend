package com.gws.crm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportStatisticsDto {
    private Long queued;
    private Long processing;
    private Long completed;
    private Long failed;

    public ExportStatisticsDto(Long queued, Long processing, Long completed, Long failed) {
        this.queued = queued == null ? 0 : queued;
        this.processing = processing == null ? 0 : processing;
        this.completed = completed == null ? 0 : completed;
        this.failed = failed == null ? 0 : failed;
    }
}

