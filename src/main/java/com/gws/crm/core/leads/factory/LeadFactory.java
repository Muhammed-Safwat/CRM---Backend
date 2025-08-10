package com.gws.crm.core.leads.factory;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.admin.entity.Admin;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.AddLeadDTO;
import com.gws.crm.core.leads.dto.ImportLeadDTO;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.mapper.PhoneNumberMapper;
import com.gws.crm.core.leads.repository.PhoneNumberRepository;
import com.gws.crm.core.lookups.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadFactory {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final InvestmentGoalRepository investmentGoalRepository;
    private final CommunicateWayRepository communicateWayRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final ChannelRepository channelRepository;
    private final ProjectRepository projectRepository;
    private final BrokerRepository brokerRepository;
    private final PhoneNumberMapper phoneNumberMapper;
    private final PhoneNumberRepository phoneNumberRepository;
    private final TeleSalesLeadFactory teleSalesLeadFactory;

    public Lead mapDtoToEntity(AddLeadDTO leadDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        Admin admin = null;

        Lead.LeadBuilder leadBuilder = Lead.builder()
                .name(leadDTO.getName())
                .status(leadStatusRepository.getReferenceById(leadDTO.getStatus()))
                .country(leadDTO.getCountry())
                .contactTime(leadDTO.getContactTime())
                .whatsappNumber(leadDTO.getWhatsappNumber())
                .email(leadDTO.getEmail())
                .jobTitle(leadDTO.getJobTitle())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .budget(leadDTO.getBudget())
                .note(leadDTO.getNote())
                .deleted(false)
                .campaignId(leadDTO.getCampaignId())
                .lastStage(leadDTO.getLastStage())
                .actions(new ArrayList<>())
                .deleted(false)
                .creator(creator);

        if (transition.getRole().equals("USER")) {
            Employee sales = employeeRepository.getReferenceById(transition.getUserId());
            admin = sales.getAdmin();
            leadBuilder.admin(admin);
            leadBuilder.salesRep(sales);
        } else {
            leadBuilder.admin((Admin) creator);
            if (leadDTO.getSalesRep() != null) {
                leadBuilder.salesRep(employeeRepository.getReferenceById(leadDTO.getSalesRep()));
            }
        }

        if (leadDTO.getInvestmentGoal() != null) {
            leadBuilder.investmentGoal(investmentGoalRepository.getReferenceById(leadDTO.getInvestmentGoal()));
        }

        if (leadDTO.getCommunicateWay() != null) {
            leadBuilder.communicateWay(communicateWayRepository.getReferenceById(leadDTO.getCommunicateWay()));
        }

        if (leadDTO.getCancelReason() != null) {
            leadBuilder.cancelReasons(cancelReasonsRepository.getReferenceById(leadDTO.getCancelReason()));
        }

        if (leadDTO.getChannel() != null) {
            leadBuilder.channel(channelRepository.getReferenceById(leadDTO.getChannel()));
        }

        if (leadDTO.getProject() != null) {
            leadBuilder.project(projectRepository.getReferenceById(leadDTO.getProject()));
        }

        if (leadDTO.getBroker() != null) {
            leadBuilder.broker(brokerRepository.getReferenceById(leadDTO.getBroker()));
        }

        Lead lead = leadBuilder.build();

        List<PhoneNumber> phoneNumbers = phoneNumberMapper.toEntityList(leadDTO.getPhoneNumbers(), lead);
        lead.setPhoneNumbers(phoneNumbers);
        return lead;
    }

    public void updateEntityFromDto(Lead existingLead, AddLeadDTO leadDTO, Transition transition) {
        User creator = userRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);
        boolean isAdmin = transition.getRole().equals("ADMIN");
       /* Admin admin = null;

        if (!isAdmin) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
            existingLead.setAdmin(admin);
        } else {
            existingLead.setAdmin((Admin) creator);
        }*/

        existingLead.setName(leadDTO.getName());
        existingLead.setStatus(leadStatusRepository.getReferenceById(leadDTO.getStatus()));
        existingLead.setCountry(leadDTO.getCountry());
        existingLead.setContactTime(leadDTO.getContactTime());
        existingLead.setWhatsappNumber(leadDTO.getWhatsappNumber());
        existingLead.setEmail(leadDTO.getEmail());
        existingLead.setJobTitle(leadDTO.getJobTitle());
        existingLead.setUpdatedAt(LocalDateTime.now());
        existingLead.setBudget(leadDTO.getBudget());
        existingLead.setNote(leadDTO.getNote());
        existingLead.setDeleted(false);
        existingLead.setCampaignId(leadDTO.getCampaignId());
        existingLead.setLastStage(leadDTO.getLastStage());
        existingLead.setCreator(creator);

        if (leadDTO.getInvestmentGoal() != null) {
            existingLead.setInvestmentGoal(investmentGoalRepository.getReferenceById(leadDTO.getInvestmentGoal()));
        }

        if (leadDTO.getCommunicateWay() != null) {
            existingLead.setCommunicateWay(communicateWayRepository.getReferenceById(leadDTO.getCommunicateWay()));
        }

        if (leadDTO.getCancelReason() != null) {
            existingLead.setCancelReasons(cancelReasonsRepository.getReferenceById(leadDTO.getCancelReason()));
        }

        if (isAdmin && !(existingLead.getId() == leadDTO.getSalesRep())) {
            existingLead.setSalesRep(employeeRepository.getReferenceById(leadDTO.getSalesRep()));
            //  leadActionService.setAssignAction(existingLead, transition);
        }

        if (leadDTO.getChannel() != null) {
            existingLead.setChannel(channelRepository.getReferenceById(leadDTO.getChannel()));
        }

        if (leadDTO.getProject() != null) {
            existingLead.setProject(projectRepository.getReferenceById(leadDTO.getProject()));
        }

        if (leadDTO.getBroker() != null) {
            existingLead.setBroker(brokerRepository.getReferenceById(leadDTO.getBroker()));
        }

        phoneNumberRepository.deleteAllById(existingLead.getPhoneNumbers().stream().map(PhoneNumber::getId).toList());

        existingLead.getPhoneNumbers().clear();

        List<PhoneNumber> updatedPhoneNumbers = phoneNumberMapper.toEntityList(leadDTO.getPhoneNumbers(), existingLead);
        existingLead.setPhoneNumbers(updatedPhoneNumbers);

    }


    public List<Lead> createLeadsList(List<ImportLeadDTO> importLeadDTOS, Transition transition) {
        List<Lead> leads = new ArrayList<>();
        User creator = userRepository.findById(transition.getUserId()).orElseThrow(NotFoundResourceException::new);
        Admin admin;
        boolean isAdmin = transition.getRole().equals("ADMIN");
        if (!isAdmin) {
            admin = employeeRepository.getReferenceById(transition.getUserId()).getAdmin();
        } else {
            admin = (Admin) creator;
        }

        Admin finalAdmin = admin;


        importLeadDTOS.forEach(leadDTO -> {
            Lead.LeadBuilder leadBuilder = Lead.builder()
                    .name(leadDTO.getName())
                    .creator(creator)
                    .admin(finalAdmin)
                    .budget(leadDTO.getBudget())
                    .note(leadDTO.getNote())
                    .country(leadDTO.getCountry())
                    .deleted(false)
                    .email(leadDTO.getEmail())
                    .whatsappNumber(leadDTO.getWhatsappNumber())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .status(leadStatusRepository.findByName(leadDTO.getStatus()));

            if (isAdmin && leadDTO.getSalesRep() != null) {
                leadBuilder.salesRep(employeeRepository.findByNameAndAdminId(leadDTO.getSalesRep(), finalAdmin.getId()));
            } else if (!isAdmin) {
                final Employee sales = (Employee) creator;
                leadBuilder.salesRep(sales);
            }

            if (leadDTO.getInvestmentGoal() != null) {
                leadBuilder.investmentGoal(investmentGoalRepository.findByNameAndAdminId(leadDTO.getInvestmentGoal(), finalAdmin.getId()));
            }

            if (leadDTO.getCommunicateWay() != null) {
                leadBuilder.communicateWay(communicateWayRepository.findByNameAndAdminId(leadDTO.getCommunicateWay(), finalAdmin.getId()));
            }

            if (leadDTO.getCancelReason() != null) {
                leadBuilder.cancelReasons(cancelReasonsRepository.findByNameAndAdminId(leadDTO.getCancelReason(), finalAdmin.getId()));
            }

            if (leadDTO.getChannel() != null) {
                leadBuilder.channel(channelRepository.findByNameAndAdminId(leadDTO.getChannel(), finalAdmin.getId()));
            }

            if (leadDTO.getProject() != null) {
                leadBuilder.project(projectRepository.findByNameAndAdminId(leadDTO.getProject(), finalAdmin.getId()));
            }

            Lead lead = leadBuilder.build();

            PhoneNumber phoneNumber = PhoneNumber.builder()
                    .lead(lead)
                    .phone(leadDTO.getPhoneNumbers())
                    .build();

            lead.setPhoneNumbers(List.of(phoneNumber));

            leads.add(lead);
        });

        return leads;
    }

}
