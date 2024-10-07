package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.LeadCriteria;
import com.gws.crm.core.leads.dto.LeadResponse;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.mapper.LeadMapper;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.service.LeadService;
import com.gws.crm.core.lockups.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

import static com.gws.crm.common.handler.ApiResponseHandler.created;
import static com.gws.crm.common.handler.ApiResponseHandler.success;
import static com.gws.crm.core.leads.spcification.LeadSpecification.filter;

@Service
@RequiredArgsConstructor
public class LeadServiceImp implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final InvestmentGoalRepository investmentGoalRepository;
    private final CommunicateWayRepository communicateWayRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final EmployeeRepository employeeRepository;
    private final ChannelRepository channelRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LeadMapper leadMapper;

    @Override
    public ResponseEntity<?> getLeads(int page, int size, Transition transition) {
        return null;
    }


    @Override
    public ResponseEntity<?> getLeadDetails(long leadId, Transition transition) {
        return null;
    }


    @Override
    public ResponseEntity<?> addLead(AddLeadDTO leadDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        Admin admin = null;

        Lead.LeadBuilder leadBuilder = Lead.builder()
                .name(leadDTO.getName())
                .status(leadStatusRepository.getReferenceById(leadDTO.getStatus()))
                .country(leadDTO.getCountry())
               // .phoneNumbers(leadDTO.getPhoneNumbers())
                .contactTime(leadDTO.getContactTime())
                .whatsappNumber(leadDTO.getWhatsappNumber())
                .email(leadDTO.getEmail())
                .jobTitle(leadDTO.getJobTitle())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .budget(leadDTO.getBudget())
                .note(leadDTO.getNote())
                .deleted(false)
                .creator(creator);

        if(!transition.getRole().equals("ADMIN")){
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
            leadBuilder.admin(admin);
        }else {
            leadBuilder.admin((Admin) creator);
        }
        Optional.of(leadDTO.getInvestmentGoal())
                .ifPresent(investmentGoalId -> leadBuilder.investmentGoal(
                        investmentGoalRepository.getReferenceById(investmentGoalId)));

        Optional.of(leadDTO.getCommunicateWay())
                .ifPresent(communicateWayId -> leadBuilder.communicateWay(
                        communicateWayRepository.getReferenceById(communicateWayId)));

        Optional.of(leadDTO.getCancelReasons())
                .ifPresent(cancelReasonsId -> leadBuilder.cancelReasons(
                        cancelReasonsRepository.getReferenceById(cancelReasonsId)));

        Optional.of(leadDTO.getSalesRep())
                .ifPresent(salesRepId -> leadBuilder.salesRep(
                        employeeRepository.getReferenceById(salesRepId)));

        Optional.of(leadDTO.getChannel())
                .ifPresent(channelId -> leadBuilder.channel(
                        channelRepository.getReferenceById(channelId)));

        Optional.of(leadDTO.getProject())
                .ifPresent(projectId -> leadBuilder.project(
                        projectRepository.getReferenceById(projectId)));

        Lead lead = leadBuilder.build();

        Lead savedLead = leadRepository.save(lead);
        LeadResponse leadResponse = leadMapper.toDTO(savedLead);
        return created(leadResponse);
    }

    @Override
    public ResponseEntity<?> updateLead(AddLeadDTO leadDTO, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteLead(String leadId, Transition transition) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAllLeads(LeadCriteria leadCriteria, Transition transition) {
        Specification<Lead> leadSpecification = filter(leadCriteria, transition);
        Pageable pageable = PageRequest.of(leadCriteria.getPage(), leadCriteria.getSize());
        Page<Lead> leadPage = leadRepository.findAll(leadSpecification, pageable);
        Page<LeadResponse> leadResponses = leadMapper.toDTOPage(leadPage);
        return success(leadResponses);
    }
}
