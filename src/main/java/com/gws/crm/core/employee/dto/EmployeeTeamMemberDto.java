package com.gws.crm.core.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class EmployeeTeamMemberDto {
    private long id;
    private String name;

    private String username;

    private String jobName;

    private String image;

    List<EmployeeTeamMemberDto> subordinates;
}