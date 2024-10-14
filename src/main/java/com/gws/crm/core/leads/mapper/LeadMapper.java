package com.gws.crm.core.leads.mapper;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.dto.LeadStatusDto;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.lookups.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LeadMapper {

    private final PhoneNumberMapper phoneNumberMapper;

    public LeadResponse toDTO(Lead lead) {
        if (lead == null) {
            return null;
        }

        LockupDTO investmentGoalDTO = lead.getInvestmentGoal() != null ?
                LockupDTO.builder()
                        .id(lead.getInvestmentGoal().getId())
                        .name(lead.getInvestmentGoal().getName())
                        .build()
                : null;

        LockupDTO communicateWayDTO = lead.getCommunicateWay() != null ?
                LockupDTO.builder()
                        .id(lead.getCommunicateWay().getId())
                        .name(lead.getCommunicateWay().getName())
                        .build()
                : null;

        LockupDTO cancelReasonsDTO = lead.getCancelReasons() != null ?
                LockupDTO.builder()
                        .id(lead.getCancelReasons().getId())
                        .name(lead.getCancelReasons().getName())
                        .build()
                : null;

        EmployeeSimpleDTO salesRepDTO = lead.getSalesRep() != null ?
                EmployeeSimpleDTO.builder()
                        .id(lead.getSalesRep().getId())
                        .name(lead.getSalesRep().getName())
                        .build()
                : null;

        LockupDTO channelDTO = lead.getChannel() != null ?
                LockupDTO.builder()
                        .id(lead.getChannel().getId())
                        .name(lead.getChannel().getName())
                        .build()
                : null;

        ProjectDTO projectDTO = lead.getProject() != null ?
                ProjectDTO.builder()
                        .id(lead.getProject().getId())
                        .name(lead.getProject().getName())
                        .build()
                : null;

        EmployeeSimpleDTO creatorDTO = lead.getCreator() != null ?
                EmployeeSimpleDTO.builder()
                        .id(lead.getCreator().getId())
                        .name(lead.getCreator().getName())
                        .build()
                : null;
        LeadStatusDto leadStatusDto = lead.getStatus() != null ?
                LeadStatusDto.builder()
                        .name(lead.getStatus().getName())
                        .id(lead.getStatus().getId())
                        .build()
                : null;
        return LeadResponse.builder()
                .id(lead.getId())
                .phoneNumbers(phoneNumberMapper.toDtoList(lead.getPhoneNumbers()))
                .name(lead.getName())
                .status(leadStatusDto)
                .country(lead.getCountry())
                .contactTime(lead.getContactTime())
                .whatsappNumber(lead.getWhatsappNumber())
                .email(lead.getEmail())
                .jobTitle(lead.getJobTitle())
                .investmentGoal(investmentGoalDTO)
                .communicateWay(communicateWayDTO)
                .updatedAt(lead.getUpdatedAt())
                .createdAt(lead.getCreatedAt())
                .cancelReasons(cancelReasonsDTO)
                .salesRep(salesRepDTO)
                .budget(lead.getBudget())
                .note(lead.getNote())
                .channel(channelDTO)
                .project(projectDTO)
                .deleted(lead.isDeleted())
                .creator(creatorDTO)
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
