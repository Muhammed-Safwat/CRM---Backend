package com.gws.crm.core.leads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountDTO {
    private long id;
    private String name;
    private Long count;


    public CountDTO(String name, Long count) {
        this.name = name;
        this.count = count;
    }
}
