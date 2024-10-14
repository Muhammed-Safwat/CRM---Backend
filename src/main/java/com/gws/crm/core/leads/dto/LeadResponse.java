package com.gws.crm.core.leads.dto;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.dto.ProjectDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class LeadResponse {

        private long id;

        private String name;

        private LeadStatusDto status;

        private List<PhoneNumberDTO> phoneNumbers;

        private String country;

        private String contactTime;

        private String whatsappNumber;

        private String email;

        private String jobTitle;

        private LookupDTO investmentGoal;

        private LookupDTO communicateWay;

        private LocalDateTime updatedAt;

        private LocalDateTime createdAt;

        private LookupDTO cancelReasons;

        private EmployeeSimpleDTO salesRep;

        private String budget;

        private String note;

        private LookupDTO channel;

        private ProjectDTO project;

        private boolean deleted;

        private EmployeeSimpleDTO creator;

}
