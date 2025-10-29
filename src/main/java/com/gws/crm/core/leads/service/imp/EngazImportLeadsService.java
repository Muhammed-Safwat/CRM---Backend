package com.gws.crm.core.leads.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.actions.entity.ActionType;
import com.gws.crm.core.actions.entity.LeadActionDetails;
import com.gws.crm.core.actions.entity.UserAction;
import com.gws.crm.core.employee.entity.Admin;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.AdminRepository;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.leads.dto.EngazActionDto;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.leads.repository.PhoneNumberRepository;
import com.gws.crm.core.lookups.entity.*;
import com.gws.crm.core.lookups.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
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
import java.util.Objects;
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
    private final InvestmentGoalRepository investmentGoalRepository;
    private final RegionRepository regionRepository;
    private final CancelReasonsRepository cancelReasonsRepository;
    private final List<EngazLeadDto> duplicatedLeads = new ArrayList<>();
    private final StageRepository stageRepository;

    @Transactional
    public ResponseEntity<?> importLeads(MultipartFile file, HttpServletRequest request, Transition transition) {
        try {
            Path savedFile = saveUploadedFile(file);
            List<EngazLeadDto> leadsDto = parseLeads(savedFile);
            logLeads(leadsDto);
            List<Lead> leads = leadsDto.stream()
                    .map(dto -> mapToEntity(dto, transition))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            leadRepository.saveAll(leads);

            if (!duplicatedLeads.isEmpty()) {
                Path dupePath = saveDuplicateLeadsFile(duplicatedLeads);
                duplicatedLeads.clear();
            }
            String fileUrl = buildFileUrl(request, savedFile.getFileName().toString());
            String message = String.format("Uploaded %d leads successfully.\nFile saved at: %s", leadsDto.size(), fileUrl);

            return success(message);
        } catch (Exception e) {
            return  error("Error while processing leads: " + e.getMessage());
        }
    }

    private Lead mapToEntity(EngazLeadDto dto, Transition transition) {

        boolean isLeadExists = phoneNumberRepository.existsByPhone(dto.getMobile());
        if (isLeadExists) {
            duplicatedLeads.add(dto);
            return null;
        }

        Admin admin = adminRepository.findById(transition.getUserId())
                .orElseThrow(NotFoundResourceException::new);

        Lead lead = new Lead();
        lead.setName(dto.getFullName());
        lead.setWhatsappNumber(dto.getWhatsappNumber());

        if(dto.getInvestmentGoal() != null){
            lead.setInvestmentGoal(resolveInvestmentGoal(dto.getInvestmentGoal(),admin));
        }

        if(dto.getRegion() != null){
            lead.setRegion(resolveRegion(dto.getRegion(),admin));
        }

        if(dto.getProject() != null){
            lead.setProject(resolveProject(dto.getProject(),admin));
        }

        lead.setLastStage(dto.getLastStage());
        lead.setJobTitle(dto.getJobTitle());
        lead.setCountry(dto.getLivingCountry());
        lead.setBudget(dto.getBudget());

        if(dto.getCancelReason() != null){
            lead.setCancelReasons(resolveCancelReason(dto.getCancelReason(),admin));
        }

        if(dto.getChannel() != null){
            lead.setChannel(resolveChannel(dto.getChannel(),admin));
        }

        if(dto.getSalesRep() != null){
            Employee sales =
                    employeeRepository.findByNameAndAdminId(dto.getSalesRep(),admin.getId()) ;
            lead.setSalesRep(sales);
        }

        lead.setLastActionComment(dto.getLastComment());
        lead.setAssignAt(parseDate(dto.getAssignDate()));
        lead.setLastActionDate(parseDate(dto.getActionDate()));
        lead.setStatus(leadStatusRepository.getReferenceById(1L));
        lead.setDeleted(false);
        lead.setArchive(false);
        lead.setCreator(admin);
        lead.setAdmin(admin);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());

        List<PhoneNumber> phones = new ArrayList<>();
        phones.add(PhoneNumber.builder().phone(dto.getMobile()).lead(lead).build());
        lead.setPhoneNumbers(phones);
        // actions
        List<UserAction> actions = dto.getActions().stream()
                .map(action -> mapToAction(action, lead, admin))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        lead.setActions(actions);
        return lead;
    }

    private Path saveDuplicateLeadsFile(List<EngazLeadDto> duplicates) throws Exception {
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = "duplicated_leads_" + timestamp + ".json";
        Path filePath = uploadDir.resolve(fileName);

        objectMapper.writeValue(filePath.toFile(), duplicates);
        return filePath;
    }
    private Channel resolveChannel(String channel, Admin creator) {
        return LookupRepositoryUtils.getOrCreateByNameAndAdmin(
                channelRepository,
                channel,
                creator.getId(),
                channelRepository::findByNameAndAdminId,
                () -> {
                    Channel r = new Channel();
                    r.setName(channel);
                    r.setAdmin(creator);
                    return r;
                }
        );
    }
    private Stage resolveStage(String stage, Admin creator) {
        return LookupRepositoryUtils.getOrCreateByNameAndAdmin(
                stageRepository,
                stage,
                creator.getId(),
                stageRepository::findByNameAndAdminId,
                () -> {
                    Stage r = new Stage();
                    r.setName(stage);
                    r.setAdmin(creator);
                    return r;
                }
        );
    }
    private CancelReasons resolveCancelReason(String cancelReason, Admin admin) {
        return LookupRepositoryUtils.getOrCreateByNameAndAdmin(
                cancelReasonsRepository,
                cancelReason,
                admin.getId(),
                cancelReasonsRepository::findByNameAndAdminId,
                () -> {
                    CancelReasons r = new CancelReasons();
                    r.setName(cancelReason);
                    r.setAdmin(admin);
                    return r;
                }
        );
    }
    private Project resolveProject(String project, Admin creator) {
        return ProjectLookupUtils.getOrCreateProject(
                projectRepository,
                project,
                creator
        );
    }
    private Region resolveRegion(String region, Admin creator) {
        return LookupRepositoryUtils.getOrCreateByNameAndAdmin(
                regionRepository,
                region,
                creator.getId(),
                regionRepository::findByNameAndAdminId,
                () -> {
                    Region r = new Region();
                    r.setName(region);
                    r.setAdmin(creator);
                    return r;
                }
        );
    }
    private InvestmentGoal resolveInvestmentGoal(String name, Admin admin) {
        return LookupRepositoryUtils.getOrCreateByNameAndAdmin(
                investmentGoalRepository,
                name,
                admin.getId(),
                investmentGoalRepository::findByNameAndAdminId,
                () -> {
                    InvestmentGoal r = new InvestmentGoal();
                    r.setName(name);
                    r.setAdmin(admin);
                    return r;
                }
        );
    }
    private ActionType resolveActionType(String stage) {
        if (stage == null) return ActionType.NO_ANSWER;

        String normalized = stage.trim().toLowerCase();

        if (normalized.contains("meeting") || normalized.contains("follow up") || normalized.contains("pipeline")) {
            return ActionType.ANSWERED;
        }

        if (normalized.contains("unreachable") ||
                normalized.contains("no answer") ||
                normalized.contains("cold call") ||
                normalized.contains("cancellation") ||
                normalized.contains("hold")) {
            return ActionType.NO_ANSWER;
        }

        return ActionType.NO_ANSWER;
    }

    private UserAction mapToAction(EngazActionDto dto,Lead lead,Admin admin) {
        if(dto.getSalesRep() == null){
            return null;
        }
        User creator = userRepository.findByName(dto.getSalesRep());
        Stage stage =  resolveStage(dto.getStage(),admin);
        lead.setStage(stage);
        leadRepository.save(lead);
        UserAction action = UserAction.builder()
                .creator(creator)
                .type(resolveActionType(dto.getStage()))
                .description(dto.getComment())
                .createdAt(parseDate(dto.getFollowDate()))
                .build();

        LeadActionDetails leadDetails = LeadActionDetails.builder()
                .userAction(action)
                .lead(lead)
                .callOutcome(stage)
                .nextActionDate(parseDate(dto.getFollowDate()))
                .callBackTime(parseDate(dto.getFollowDate()))
                .comment(dto.getComment())
                .build();

        action.setLeadDetails(leadDetails);

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
