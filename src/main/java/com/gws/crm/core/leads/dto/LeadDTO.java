package com.gws.crm.core.leads.dto;

import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.entity.Project;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class LeadDTO {

    private long id;

    private String name;

    private boolean isCold;

    private String country;

    private List<PhoneNumberDTO> phoneNumbers;

    private String contactTime;

    private String whatsappNumber;

    private String email;

    private String jobTitle;

    private String investmentGoal;

    private String communicateWay;

    private LocalDateTime lastStageDate;

    private String cancelReasons;

    private String salesRep;

    private String budget;

    private String note;

    private Channel channel;

    private Project project;

}
