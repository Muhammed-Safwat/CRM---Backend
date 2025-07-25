package com.gws.crm.core.resale.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResaleCriteria {
    private int id;
    private String keyword;
    private String name;
    private String phone;
    private String email;
    private List<Long> project;
    private String BUA;
    private String phase;
    private String code;
    private List<Long> status;
    private Long type;
    private List<Long> category;
    private List<Long> property;
    private String note;
    private LocalDate createdAt;
    private List<Long> creator;
    private List<Long> salesRep;
    private boolean deleted;
    private boolean myLead;
    private Boolean delayed;
    private List<Long> subordinates;
    @NotNull
    private Integer page;
    @NotNull
    private Integer size;
}
