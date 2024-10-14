package com.gws.crm.core.leads.mapper;

import com.gws.crm.core.leads.dto.PhoneNumberDTO;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class PhoneNumberMapper {

    public PhoneNumber toEntity(PhoneNumberDTO phoneNumberDTO, BaseLead lead){
        if (phoneNumberDTO == null || lead == null) {
            return null;
        }
        return PhoneNumber.builder()
                .phone(phoneNumberDTO.getPhone())
                .code(phoneNumberDTO.getCode())
                .lead(lead)
                .build();
    }

    public List<PhoneNumber> toEntityList(List<PhoneNumberDTO> phoneNumbersDTOs, BaseLead lead){
        if (phoneNumbersDTOs == null || lead == null) {
            return null;
        }
        return phoneNumbersDTOs.stream()
                .map(dto -> toEntity(dto, lead))
                .collect(Collectors.toList());
    }

    public PhoneNumberDTO toDto(PhoneNumber phoneNumber){
        if (phoneNumber == null) {
            return null;
        }
        return PhoneNumberDTO.builder()
                .phone(phoneNumber.getPhone())
                .code(phoneNumber.getCode())
                .build();
    }

    public List<PhoneNumberDTO> toDtoList(List<PhoneNumber> phoneNumbers){
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            return null;
        }
        return phoneNumbers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
