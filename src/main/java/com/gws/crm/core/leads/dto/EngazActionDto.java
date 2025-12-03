package com.gws.crm.core.leads.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EngazActionDto {

    @JsonProperty("Client name")
    private String clientName;

    @JsonProperty("Mobile")
    private String mobile;

    @JsonProperty("Stage")
    private String stage;

    @JsonProperty("Follow Date")
    private String followDate;

    @JsonProperty("Sales Rep")
    private String salesRep;

    @JsonProperty("comment")
    private String comment;
}
