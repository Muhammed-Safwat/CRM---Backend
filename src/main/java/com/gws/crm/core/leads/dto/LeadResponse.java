package com.gws.crm.core.leads.dto;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.core.employee.dto.EmployeeDto;
import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.lookups.dto.*;
import com.gws.crm.core.lookups.entity.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

        private LockupDTO investmentGoal;

        private LockupDTO communicateWay;

        private LocalDateTime updatedAt;

        private LocalDateTime createdAt;

        private LockupDTO cancelReasons;

        private EmployeeSimpleDTO salesRep;

        private String budget;

        private String note;

        private LockupDTO channel;

        private ProjectDTO project;

        private boolean deleted;

        private EmployeeSimpleDTO creator;

}
