package com.gws.crm.core.leads.mapper;

import com.gws.crm.core.employee.dto.EmployeeSimpleDTO;
import com.gws.crm.core.leads.dto.PreLeadResponse;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.lookups.dto.LookupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PreLeadMapper {

    private final PhoneNumberMapper phoneNumberMapper;

    public PreLeadResponse toDTO(PreLead lead) {
        if (lead == null) {
            return null;
        }

        LookupDTO channelDTO = lead.getChannel() != null ?
                LookupDTO.builder()
                        .id(lead.getChannel().getId())
                        .name(lead.getChannel().getName())
                        .build()
                : null;

        LookupDTO projectDTO = lead.getProject() != null ?
                LookupDTO.builder()
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

        return PreLeadResponse.builder()
                .id(lead.getId())
                .phoneNumbers(phoneNumberMapper.toDtoList(lead.getPhoneNumbers()))
                .name(lead.getName())
                .country(lead.getCountry())
                .email(lead.getEmail())
                .updatedAt(lead.getUpdatedAt())
                .createdAt(lead.getCreatedAt())
                .importedAt(lead.getImportedAt())
                .channel(channelDTO)
                .project(projectDTO)
                .imported(lead.isImported())
                .note(lead.getNote())
                .creator(creatorDTO)
                .link(lead.getLink())
                .assignedTo(lead.getAssignedTo())
                .deleted(lead.isDeleted())
                .delayed(lead.isDelay())
                .build();
    }


    public List<PreLeadResponse> toDTOList(List<PreLead> leads) {
        return leads.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<PreLeadResponse> toDTOPage(Page<PreLead> leadPage) {
        return leadPage.map(this::toDTO);
    }
}
