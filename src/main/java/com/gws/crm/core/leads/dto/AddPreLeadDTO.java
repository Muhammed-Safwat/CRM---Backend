package com.gws.crm.core.leads.dto;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String name;

    private String country;

    @NotNull
    private List<PhoneNumberDTO> phoneNumbers;

    private String note;

    private Long channel;

    private Long category;

    private Long project;

    private String link;
}
