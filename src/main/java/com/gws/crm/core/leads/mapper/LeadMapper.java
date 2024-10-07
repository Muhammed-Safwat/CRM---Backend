package com.gws.crm.core.leads.mapper;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.dto.LeadStatusDto;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.lockups.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LeadMapper {

    public LeadResponse toDTO(Lead lead) {
        if (lead == null) {
            return null;
        }

        return LeadResponse.builder()
                .id(lead.getId())
                .name(lead.getName())
                .status(LeadStatusDto.builder().name(lead.getName()).id(lead.getId()).build())
                .country(lead.getCountry())
                .contactTime(lead.getContactTime())
                .whatsappNumber(lead.getWhatsappNumber())
                .email(lead.getEmail())
                .jobTitle(lead.getJobTitle())
                .investmentGoal(
                        InvestmentGoalDTO.builder()
                                .id(lead.getInvestmentGoal().getId())
                                .name(lead.getInvestmentGoal().getName())
                                .build()
                ).communicateWay(
                        CommunicateWayDTO.builder()
                                .id(lead.getCommunicateWay().getId())
                                .name(lead.getCommunicateWay().getName())
                                .build()
                )
                .updatedAt(lead.getUpdatedAt())
                .createdAt(lead.getCreatedAt())
                .cancelReasons(
                        CancelReasonsDTO.builder()
                                .id(lead.getCancelReasons().getId())
                                .name(lead.getCancelReasons().getName())
                                .build()
                )
                .salesRep(EmployeeSimpleDTO.builder()
                        .id(lead.getSalesRep().getId())
                        .name(lead.getSalesRep().getName())
                        .build()
                )
                .budget(lead.getBudget())
                .note(lead.getNote())
                .channel(
                        ChannelDTO.builder()
                                .id(lead.getChannel().getId())
                                .name(lead.getChannel().getName())
                                .build()
                )
                .project(
                        ProjectDTO.builder()
                                .id(lead.getProject().getId())
                                .name(lead.getProject().getName())
                                .build()
                )
                .deleted(lead.isDeleted())
                .creator(EmployeeSimpleDTO.builder()
                        .id(lead.getCreator().getId())
                        .name(lead.getCreator().getName())
                        .build()
                )
                .build();
    }

    public List<LeadResponse> toDTOList(List<Lead> leads) {
        return leads.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<LeadResponse> toDTOPage(Page<Lead> leadPage) {
        return leadPage.map(this::toDTO);
    }
}
