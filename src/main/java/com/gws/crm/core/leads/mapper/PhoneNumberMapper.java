package com.gws.crm.core.leads.mapper;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.gws.crm.common.exception.InvalidPhoneNumberException;
import com.gws.crm.common.utils.PhoneNumberUtilsService;
import com.gws.crm.core.leads.dto.PhoneNumberDTO;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.gws.crm.common.utils.CountryCodeMapper.getRegionCode;

@Component
@RequiredArgsConstructor
public class PhoneNumberMapper {

    private final PhoneNumberUtilsService phoneNumberUtilsService;
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public PhoneNumber toEntity(PhoneNumberDTO phoneNumberDTO, BaseLead lead) throws NumberParseException {
        if (phoneNumberDTO == null || lead == null) {
            return null;
        }
        Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phoneNumberDTO.getPhone(),
                getRegionCode(phoneNumberDTO.getCode()));
/*
        if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
            throw new InvalidPhoneNumberException();
        }

*/
        return PhoneNumber.builder()
                .phone(String.valueOf(phoneNumber.getNationalNumber()))
                .code(phoneNumberDTO.getCode())
                .lead(lead)
                .build();
    }

    public List<PhoneNumber> toEntityList(List<PhoneNumberDTO> phoneNumbersDTOs, BaseLead lead) {
        if (phoneNumbersDTOs == null || lead == null) {
            return null;
        }
        return phoneNumbersDTOs.stream()
                .map(dto -> {
                    try {
                        return toEntity(dto, lead);
                    } catch (NumberParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public PhoneNumberDTO toDto(PhoneNumber phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return PhoneNumberDTO.builder()
                .phone(phoneNumber.getPhone())
                .code(phoneNumber.getCode())
                .build();
    }

    public List<PhoneNumberDTO> toDtoList(List<PhoneNumber> phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            return null;
        }
        return phoneNumbers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
