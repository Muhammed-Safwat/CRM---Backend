package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.employee.entity.Admin;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.EngazActionDto;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.repository.PhoneNumberRepository;
import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.entity.LeadStatus;
import com.gws.crm.core.lookups.entity.Project;
import com.gws.crm.core.lookups.repository.ChannelRepository;
import com.gws.crm.core.lookups.repository.LeadStatusRepository;
import com.gws.crm.core.lookups.repository.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gws.crm.core.leads.dto.EngazLeadDto;
import org.springframework.web.servlet.View;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gws.crm.common.handler.ApiResponseHandler.error;
import static com.gws.crm.common.handler.ApiResponseHandler.success;


@Service
@AllArgsConstructor
public class EngazImportLeadsService {

    private final ObjectMapper objectMapper ;
    private final LeadRepository leadRepository;
    private final ProjectRepository projectRepository;
    private final ChannelRepository channelRepository;
    private final EmployeeRepository employeeRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final UserRepository userRepository;
    private final LeadStatusRepository leadStatusRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public ResponseEntity<?> importLeads(MultipartFile file, HttpServletRequest request, Transition transition) {
        try {
            Path savedFile = saveUploadedFile(file);
            List<EngazLeadDto> leadsDto = parseLeads(savedFile);
            logLeads(leadsDto);
            List<Lead> leads = leadsDto.stream()
                    .map(dto -> mapToEntity(dto, transition))
                    .collect(Collectors.toList());
           // leadRepository.saveAll(leads);
            String fileUrl = buildFileUrl(request, savedFile.getFileName().toString());
            String message = String.format("Uploaded %d leads successfully.\nFile saved at: %s", leadsDto.size(), fileUrl);

            return success(message);
        } catch (Exception e) {
            return  error("Error while processing leads: " + e.getMessage());
        }
    }

    private Lead mapToEntity(EngazLeadDto dto, Transition transition) {
        Admin creator = adminRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        Lead lead = new Lead();
        lead.setName(dto.getFullName());
        lead.setBudget(dto.getBudget());
        lead.setJobTitle(dto.getJobTitle());
        lead.setLastActionComment(dto.getLastComment());
        lead.setCountry(dto.getRegion());
        lead.setWhatsappNumber(dto.getWhatsappNumber());
        lead.setAssignAt(parseDate(dto.getAssignDate()));
        lead.setLastActionDate(parseDate(dto.getActionDate()));
        lead.setStatus(leadStatusRepository.getReferenceById(1L));
        lead.setDeleted(false);
        lead.setActions(new ArrayList<>());
        lead.setCreator(creator);
        lead.setAdmin(creator);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());

        // mock relations (replace with lookups)
        List<PhoneNumber> phones = new ArrayList<>();
        PhoneNumber phoneNumber = PhoneNumber.builder().phone(dto.getMobile()).lead(lead).build();
        phones.add(phoneNumber);
        lead.setPhoneNumbers(phones);
        if(dto.getProject() != null){
            lead.setProject(projectRepository.findByNameAndAdminId(dto.getProject(),transition.getUserId()));
        }else {
            Project proj = Project.builder()
                    .name(dto.getProject())
                    .deleted(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            lead.setProject(proj);
        }
        if(dto.getProject() != null){
            lead.setChannel(channelRepository.findByName(dto.getChannel()).orElseThrow());
        }else {
            Channel channel  = Channel.builder()
                    .name(dto.getChannel())
                    .deleted(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            lead.setChannel(channel);
        }
        lead.setChannel(channelRepository.findByName(dto.getChannel()).orElse(null));
        // lead.setSalesRep(employeeRepository.findByName(dto.getSalesRep()).orElse(null));

        // map actions if any
        if (dto.getActions() != null && !dto.getActions().isEmpty()) {
            List<UserAction> actions = dto.getActions().stream()
                    .map(this::mapToAction)
                    .collect(Collectors.toList());
            lead.setActions(actions);
        }

        return lead;
    }

    private UserAction mapToAction(EngazActionDto dto) {
        UserAction action = new UserAction();
        // action.setComment(dto.getComment());
        action.setCreatedAt(parseDate(dto.getFollowDate()));
        // action.setStage(dto.getStage());
        return action;
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            if (dateStr == null || dateStr.isBlank()) return null;
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return null;
        }
    }

    private Path saveUploadedFile(MultipartFile file) throws Exception {
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = "leads_" + timestamp + ".json";
        Path filePath = uploadDir.resolve(fileName);

        file.transferTo(filePath.toFile());
        return filePath;
    }

    private List<EngazLeadDto> parseLeads(Path filePath) throws Exception {
        return objectMapper.readValue(filePath.toFile(), new TypeReference<>() {});
    }

    private void logLeads(List<EngazLeadDto> leads) {
        leads.forEach(lead -> {
            System.out.println("Lead: " + lead.toString());
            if (lead.getActions() != null) {
                lead.getActions().forEach(a ->
                        System.out.println("  → Action: " + a.getStage() + " | " + a.getComment()));
            }
        });
    }

    private String buildFileUrl(HttpServletRequest request, String fileName) {
        return String.format("%s://%s:%d/uploads/%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                fileName);
    }

}
