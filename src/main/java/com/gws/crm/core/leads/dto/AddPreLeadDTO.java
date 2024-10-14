package com.gws.crm.core.leads.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPreLeadDTO {
    private String name;

    private String country;

    private List<PhoneNumberDTO> phoneNumbers;

    private String email;

    private String jobTitle;

    private String note;

    private Long status;

}
