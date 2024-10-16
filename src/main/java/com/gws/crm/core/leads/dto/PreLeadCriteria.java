package com.gws.crm.core.leads.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PreLeadCriteria {
    private String keyword;
    private String country;
    private Long id;
    private boolean deleted;
    private boolean myLead;
    private List<Long> creator;
    private List<Integer> campaignId;
    private LocalDate createdAt;
    private int page;
    private int size;
}
