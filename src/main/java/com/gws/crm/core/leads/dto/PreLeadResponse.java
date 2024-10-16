package com.gws.crm.core.leads.dto;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.lookups.dto.LookupDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PreLeadResponse {

    private long id;

    private String name;

    private List<PhoneNumberDTO> phoneNumbers;

    private String country;

    private String email;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private String note;

    private LookupDTO channel;

    private LookupDTO project;

    private boolean deleted;

    private boolean imported;

    private EmployeeSimpleDTO creator;

    private String link;
}
