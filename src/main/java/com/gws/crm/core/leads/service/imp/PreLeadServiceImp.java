package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.handler.ApiResponseHandler;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.AddPreLeadDTO;
import com.gws.crm.core.leads.dto.PreLeadCriteria;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.repository.PreLeadRepository;
import com.gws.crm.core.leads.service.PreLeadService;
import com.gws.crm.core.lockups.repository.LeadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreLeadServiceImp implements PreLeadService {

    private final PreLeadRepository preLeadRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PhoneNumberMapper phoneNumberMapper;

    @Override
    public ResponseEntity<?> getAllPreLead(PreLeadCriteria preLeadCriteria, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> addPreLead(AddPreLeadDTO preLeadDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Admin admin;

        if (!transition.getRole().equals("ADMIN")) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
        } else {
            admin = (Admin) creator;
        }


        PreLead preLead = PreLead.builder()
                .name(preLeadDTO.getName())
                .email(preLeadDTO.getEmail())
                .country(preLeadDTO.getCountry())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .note(preLeadDTO.getNote())
                .jobTitle(preLeadDTO.getJobTitle())
                .status(leadStatusRepository.findByName(preLeadDTO.getName()))
                .creator(creator)
                .admin(admin)
                .build();

        List<PhoneNumber> phoneNumbers = phoneNumberMapper.toEntityList(preLeadDTO.getPhoneNumbers(), preLead);

        preLead.setPhoneNumbers(phoneNumbers);
        preLeadRepository.save(preLead);
        return ApiResponseHandler.success("Pre Lead added ");
    }
}
