package com.gws.crm.core.leads.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PreLeadCriteria {
    private String keyword;
    private String country;
    private Long id;
    private Boolean deleted;
    private Boolean imported;
    private Boolean myLead;
    private List<Long> creator;
    private List<Integer> campaignId;
    private LocalDate createdAt;
    private List<Long> subordinates;
    private int page;
    private int size;
    private Boolean delayed;
    private Boolean archived;
}
