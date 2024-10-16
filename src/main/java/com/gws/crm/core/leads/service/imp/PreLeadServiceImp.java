package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.common.handler.ApiResponseHandler;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.*;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.entity.PreLead;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.mapper.PreLeadMapper;
import com.gws.crm.core.leads.repository.BaseLeadRepository;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.repository.PreLeadRepository;
import com.gws.crm.core.leads.service.PreLeadService;
import com.gws.crm.core.lookups.entity.Project;
import com.gws.crm.core.lookups.repository.CampaignRepository;
import com.gws.crm.core.lookups.repository.ChannelRepository;
import com.gws.crm.core.lookups.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.common.utils.ExcelFileUtils.generateHeader;
import static com.gws.crm.core.leads.spcification.PreLeadSpecification.filter;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreLeadServiceImp implements PreLeadService {

    private final PreLeadRepository preLeadRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PhoneNumberMapper phoneNumberMapper;
    private final PreLeadMapper preLeadMapper;
    private final ProjectRepository projectRepository;
    private final ChannelRepository channelRepository;
    private final BaseLeadRepository baseLeadRepository;

    @Override
    public ResponseEntity<?> getAllPreLead(PreLeadCriteria preLeadCriteria, Transition transition) {
        Specification<PreLead> leadSpecification = filter(preLeadCriteria, transition);
        log.info("Page size ====> {}", preLeadCriteria);
        Pageable pageable = PageRequest.of(preLeadCriteria.getPage(), preLeadCriteria.getSize());
        log.info("Page size ====> {}", pageable);
        Page<PreLead> leadPage = preLeadRepository.findAll(leadSpecification, pageable);
        Page<PreLeadResponse> leadResponses = preLeadMapper.toDTOPage(leadPage);
        return success(leadResponses);
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
                .country(preLeadDTO.getCountry())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .note(preLeadDTO.getNote())
                .creator(creator)
                .admin(admin)
                .imported(false)
                .link(preLeadDTO.getLink())
                .project(projectRepository.getReferenceById(preLeadDTO.getProject()))
                .channel(channelRepository.getReferenceById(preLeadDTO.getChannel()))
                .build();

        List<PhoneNumber> phoneNumbers = phoneNumberMapper.toEntityList(preLeadDTO.getPhoneNumbers(), preLead);

        preLead.setPhoneNumbers(phoneNumbers);
        preLeadRepository.save(preLead);
        return ApiResponseHandler.success("Pre Lead added successfully");
    }

    @Override
    public ResponseEntity<?> deletePreLead(Long leadId, Transition transition) {
        baseLeadRepository.deleteLead(leadId);
        return success("Lead Deleted Successfully");
    }

    @Override
    public ResponseEntity<?> restorePreLead(Long leadId, Transition transition) {
        baseLeadRepository.restoreLead(leadId);
        return success("Lead Deleted Successfully");
    }
}
