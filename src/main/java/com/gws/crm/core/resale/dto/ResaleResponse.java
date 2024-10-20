package com.gws.crm.core.resale.dto;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.dto.ProjectDTO;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResaleResponse {
    private long id;
    private String name;
    private LookupDTO status;
    private LookupDTO type;
    private String phone;
    private String email;
    private ProjectDTO project;
    private String BUA;
    private String Phase;
    private String Code;
    private LookupDTO category;
    private LookupDTO property;
    private String note;
    private LocalDateTime createdAt;
    private EmployeeSimpleDTO creator;
    private EmployeeSimpleDTO salesRep;
    private LocalDateTime updatedAt;
    private boolean deleted;
}
