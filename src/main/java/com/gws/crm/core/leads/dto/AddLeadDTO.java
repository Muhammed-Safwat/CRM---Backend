package com.gws.crm.core.leads.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AddLeadDTO {

    private String name;

    private String country;

    private List<PhoneNumberDTO> phoneNumbers;

    private String contactTime;

    private String whatsappNumber;

    private String email;

    private String jobTitle;

    private String budget;

    private String note;

    private Long status;

    private Long investmentGoal;

    private Long communicateWay;

    private Long cancelReasons;

    private Long salesRep;

    private Long channel;

    private Long project;

}
