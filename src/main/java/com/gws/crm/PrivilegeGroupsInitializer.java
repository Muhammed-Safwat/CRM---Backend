package com.gws.crm;

import com.gws.crm.authentication.entity.Privilege;
import com.gws.crm.authentication.entity.PrivilegeGroup;
import com.gws.crm.authentication.entity.Role;
import com.gws.crm.authentication.repository.RoleRepository;
import com.gws.crm.core.admin.entity.SuperAdmin;
import com.gws.crm.core.admin.repository.SuperAdminRepository;
import com.gws.crm.core.leads.repository.PrivilegeGroupRepository;
import com.gws.crm.core.resale.entities.ResaleStatus;
import com.gws.crm.core.resale.entities.ResaleType;
import com.gws.crm.core.resale.repository.ResaleStatusRepository;
import com.gws.crm.core.resale.repository.ResaleTypeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PrivilegeGroupsInitializer {

    private final PrivilegeGroupRepository privilegeGroupRepository;

    private final SuperAdminRepository superAdminRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ResaleStatusRepository resaleStatusRepository;

    private final ResaleTypeRepository resaleTypeRepository;

    void createSuperAdmin() {
        Set<Role> roles = new HashSet<>();
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN");
        log.info(superAdminRole.toString());
        roles.add(superAdminRole);

        SuperAdmin superAdmin = SuperAdmin.builder()
                .name("Muhammed Safwat")
                .username("muhammedsafwat@gmail.com")
                .password(passwordEncoder.encode("superadmin12345"))
                .enabled(true)
                .locked(false)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .accountNonExpired(LocalDateTime.of(9999, 12, 31, 23, 59, 59))
                .credentialsNonExpired(LocalDateTime.of(9999, 12, 31, 23, 59, 59))
                .roles(roles)
                .privileges(new HashSet<>())
                .build();

       superAdminRepository.save(superAdmin);
    }

    void createAdmin() {

    }

    @PostConstruct
    public void initializeData() {
        //addResaleStatuses();
        //addResaleTypes();
       // createSuperAdmin();
//        addSalesDirectorRole();
//        addTeamLeadRole();
//            salesRep();
//        marketingRole();
//        dataEntry();
//        accountant();
//        callCenterAgent();
//        customerServiceManagement();
//        customerServiceAgent();
//        marketingManger();
//         salesManger();
//        qualityControl();
//        salesAdminRole();
//        teleSalesLeader();
//        teleSalesAgent();
//        customerServiceTeamLeader();
//        branchManger();
    }

    private void addResaleStatuses() {
        List<String> statuses = List.of(
                "For Sale",
                "Not For Sale",
                "In Deal",
                "Sold",
                "For Rent",
                "Rented",
                "Showing",
                "Follow Up",
                "Rejected",
                "Expired",
                "Recycled"
        );

        for (String statusName : statuses) {
            ResaleStatus resaleStatus = ResaleStatus.builder()
                    .name(statusName)
                    .build();
            resaleStatusRepository.save(resaleStatus);

        }
    }

    // Add all predefined ResaleType values to the database
    private void addResaleTypes() {
        List<String> types = List.of("Sale", "Rent");

        for (String typeName : types) {
            ResaleType resaleType = ResaleType.builder()
                    .name(typeName)
                    .build();
            resaleTypeRepository.save(resaleType);
        }
    }

    void addSalesDirectorRole() {
        // Create a list to hold the Privileges for the Sales Director role
        List<Privilege> privileges = new ArrayList<>();
        // Save the privilege group for "Sales Director"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Sales Director")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CLIENT_INPUT")
                .groupName("Leads")
                .labelValue("Client Input")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNIT_INPUTS")
                .groupName("Units")
                .labelValue("Unit Inputs")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_RESERVATION_AND_DEAL_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Reservation and Deal Requests")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Reports")
                .labelValue("Market Reports")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL_REPORT")
                .groupName("Reports")
                .labelValue("Channel Report")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        // Deals
        privileges.add(Privilege.builder()
                .name("DEALS")
                .groupName("Deals")
                .labelValue("Deals")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("PROJECTS")
                .groupName("Others")
                .labelValue("Projects")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("REGIONS")
                .groupName("Others")
                .labelValue("Regions")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("AREA")
                .groupName("Others")
                .labelValue("Area")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MYPROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("STAGE")
                .groupName("Others")
                .labelValue("Stage")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Landpage")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("USERS")
                .groupName("Others")
                .labelValue("Users")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LEAD_GENERATION")
                .groupName("Others")
                .labelValue("Lead Generation")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CAMPAIGN")
                .groupName("Others")
                .labelValue("Campaign")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_ACTION_ON_TEAM_MEMBERS")
                .groupName("Others")
                .labelValue("Add Action on Team Members")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SEND_CIL")
                .groupName("Others")
                .labelValue("Send CIL")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CUSTOMER_SERVICES")
                .groupName("Others")
                .labelValue("Customer Services")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MULTI_ACTION")
                .groupName("Others")
                .labelValue("Multi Action")
                .defaultValue(false).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("FILTER_BY_CHANNEL")
                .groupName("Controls")
                .labelValue("Filter by Channel")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_TO_QUEUE")
                .groupName("Controls")
                .labelValue("Assign to Queue")
                .defaultValue(true).privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void addTeamLeadRole() {
        // Create a list to hold the Privileges for the Team Leader role
        List<Privilege> privileges = new ArrayList<>();
        // Save the privilege group for "Team Leader"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Team Leader")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_UNIT")
                .groupName("Units")
                .labelValue("Edit Unit")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_HOLD_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Hold Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_RESERVATION_AND_DEAL_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Reservation and Deal Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Deals
        privileges.add(Privilege.builder()
                .name("DEALS")
                .groupName("Deals")
                .labelValue("Deals")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEAL_REQUEST")
                .groupName("Deals")
                .labelValue("Deal Request")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("MYPROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_ACTION_ON_TEAM_MEMBERS")
                .groupName("Others")
                .labelValue("Add Action on Team Members")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESERVATION_REQUEST")
                .groupName("Others")
                .labelValue("Reservation Request")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_SELLER_CHART")
                .groupName("Others")
                .labelValue("Top Seller Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_ACTION_CHART")
                .groupName("Others")
                .labelValue("Top Action Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privilegeGroupRepository.save(privilegeGroup);
    }

    void salesRep() {

        // Create a list to hold the Privileges for the Sales Rep role
        List<Privilege> privileges = new ArrayList<>();
        // Save the privilege group for "Sales Rep"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Resale")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_UNIT")
                .groupName("Units")
                .labelValue("Edit Unit")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Brokers
        privileges.add(Privilege.builder()
                .name("ADD_BROKER")
                .groupName("Brokers")
                .labelValue("Add Broker")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("MYPROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_SELLER_CHART")
                .groupName("Others")
                .labelValue("Top Seller Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_ACTION_CHART")
                .groupName("Others")
                .labelValue("Top Action Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("FILTER_BY_CHANNEL")
                .groupName("Controls")
                .labelValue("Filter by Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("PREVIEW_CHANNEL")
                .groupName("Controls")
                .labelValue("Preview Channel")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_CHANNEL")
                .groupName("Controls")
                .labelValue("Edit Channel")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privilegeGroupRepository.save(privilegeGroup);
    }

    void marketingRole() {

        // Create a list to hold the Privileges for the Marketing role
        List<Privilege> privileges = new ArrayList<>();

        // Save the privilege group for "Marketing"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Marketing")
                .privileges(privileges)
                .build();

        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LEAD_GENRATION")
                .groupName("Leads")
                .labelValue("Lead Generation")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Reports")
                .labelValue("Market Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL_REPORTS")
                .groupName("Reports")
                .labelValue("Channel Reports")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Land Page")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CAMPAIGN")
                .groupName("Others")
                .labelValue("Campaign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privilegeGroupRepository.save(privilegeGroup);
    }

    void dataEntry() {

        // Create a list to hold the Privileges for the Data Entry role
        List<Privilege> privileges = new ArrayList<>();

        // Create and save the privilege group for "Data Entry"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Data Entry")
                .privileges(privileges)
                .build();

        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("REGIONS")
                .groupName("Others")
                .labelValue("Regions")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("STAGE")
                .groupName("Others")
                .labelValue("Stage")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Land Page")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void accountant() {

        // Create a list to hold the Privileges for the Accountant role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Accountant"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Accountant")
                .privileges(privileges)
                .build();

        // Leads
        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNITS")
                .groupName("Units")
                .labelValue("Units")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_RESERVATION_AND_DEAL_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Reservation and Deal Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("CUSTOMER_SERVICE_COMMENT")
                .groupName("Others")
                .labelValue("Customer Service Comment")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCOUNTANT_ACTIONS")
                .groupName("Others")
                .labelValue("Accountant Actions")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privilegeGroupRepository.save(privilegeGroup);
    }

    void callCenterAgent() {

        // Create a list to hold the Privileges for the Call Center Agent role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Call Center Agent"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Call Center Agent")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Others")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void customerServiceManagement() {
        // Create a list to hold the Privileges for the Customer Service Management role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Customer Service Management"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Customer Service Management")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_CUSTOMER_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Customer Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNITS")
                .groupName("Units")
                .labelValue("Units")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("ALL_CUSTOMERS")
                .groupName("Others")
                .labelValue("All Customers")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("FRESH_CUSTOMERS")
                .groupName("Others")
                .labelValue("Fresh Customers")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MY_CUSTOMERS")
                .groupName("Others")
                .labelValue("My Customers")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CS_STAGE")
                .groupName("Others")
                .labelValue("CS Stage")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CS_STAGE_TYPE")
                .groupName("Others")
                .labelValue("CS Stage Type")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCOUNTANT_ACTIONS")
                .groupName("Others")
                .labelValue("Accountant Actions")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_CUSTOMERS")
                .groupName("Others")
                .labelValue("Assign Customers")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void customerServiceAgent() {

        // Create a list to hold the Privileges for the Customer Service Agent role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Customer Service Agent"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Customer Service Agent")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_CUSTOMER_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Customer Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNITS")
                .groupName("Units")
                .labelValue("Units")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("MY_CUSTOMERS")
                .groupName("Others")
                .labelValue("My Customers")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCOUNTANT_ACTIONS")
                .groupName("Others")
                .labelValue("Accountant Actions")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_CUSTOMERS")
                .groupName("Others")
                .labelValue("Assign Customers")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void marketingManger() {
        // Create a list to hold the Privileges for the Marketing Manager role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Marketing Manager"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Marketing Manager")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LEAD_GENERATION")
                .groupName("Leads")
                .labelValue("Lead Generation")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Reports")
                .labelValue("Market Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL_REPORTS")
                .groupName("Reports")
                .labelValue("Channel Reports")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Landpage")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Others")
                .labelValue("Market Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CAMPAIGN")
                .groupName("Others")
                .labelValue("Campaign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void salesManger() {

        // Create a list to hold the Privileges for the Sales Manager role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Sales Manager"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Resale Manager")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CLIENT_INPUT")
                .groupName("Leads")
                .labelValue("Client Input")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LEAD_GENERATION")
                .groupName("Leads")
                .labelValue("Lead Generation")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNIT_INPUTS")
                .groupName("Units")
                .labelValue("Unit Inputs")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_HOLD_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Hold Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Reports")
                .labelValue("Market Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Deals
        privileges.add(Privilege.builder()
                .name("DEALS")
                .groupName("Deals")
                .labelValue("Deals")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEAL_REQUEST")
                .groupName("Deals")
                .labelValue("Deal Request")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("PROJECTS")
                .groupName("Others")
                .labelValue("Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("REGIONS")
                .groupName("Others")
                .labelValue("Regions")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MY_PROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("STAGE")
                .groupName("Others")
                .labelValue("Stage")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Landpage")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("USERS")
                .groupName("Others")
                .labelValue("Users")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CAMPAIGN")
                .groupName("Others")
                .labelValue("Campaign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_ACTION_ON_TEAM_MEMBERS")
                .groupName("Others")
                .labelValue("Add Action On Team Members")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESERVATION_REQUEST")
                .groupName("Others")
                .labelValue("Reservation Request")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_SELLER_CHART")
                .groupName("Others")
                .labelValue("Top Seller Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_ACTION_CHART")
                .groupName("Others")
                .labelValue("Top Action Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("FILTER_BY_CHANNEL")
                .groupName("Controls")
                .labelValue("Filter By Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_TO_QUEUE")
                .groupName("Controls")
                .labelValue("Assign To Queue")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void qualityControl() {
        // Create a list to hold the Privileges for the Quality Control role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Quality Control"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Quality Control")
                .privileges(privileges)
                .build();

        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CLIENT_INPUT")
                .groupName("Leads")
                .labelValue("Client Input")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNIT_INPUTS")
                .groupName("Units")
                .labelValue("Unit Inputs")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_RESERVATION_AND_DEAL_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Reservation And Deal Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Reports")
                .labelValue("Market Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL_REPORT")
                .groupName("Reports")
                .labelValue("Channel Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Deals
        privileges.add(Privilege.builder()
                .name("DEALS")
                .groupName("Deals")
                .labelValue("Deals")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("PROJECTS")
                .groupName("Others")
                .labelValue("Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("REGIONS")
                .groupName("Others")
                .labelValue("Regions")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("AREA")
                .groupName("Others")
                .labelValue("Area")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MY_PROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("STAGE")
                .groupName("Others")
                .labelValue("Stage")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Landpage")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("USERS")
                .groupName("Others")
                .labelValue("Users")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LEAD_GENERATION")
                .groupName("Others")
                .labelValue("Lead Generation")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CAMPAIGN")
                .groupName("Others")
                .labelValue("Campaign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_ACTION_ON_TEAM_MEMBERS")
                .groupName("Others")
                .labelValue("Add Action On Team Members")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SEND_CIL")
                .groupName("Others")
                .labelValue("Send CIL")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CUSTOMER_SERVICES")
                .groupName("Others")
                .labelValue("Customer Services")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("FILTER_BY_CHANNEL")
                .groupName("Controls")
                .labelValue("Filter By Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_TO_QUEUE")
                .groupName("Controls")
                .labelValue("Assign To Queue")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privilegeGroupRepository.save(privilegeGroup);
    }

    void salesAdminRole() {
        // Create a list to hold the Privileges for the Sales Admin role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Sales Admin"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Sales Admin")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CLIENT_INPUT")
                .groupName("Leads")
                .labelValue("Client Input")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNIT_INPUTS")
                .groupName("Units")
                .labelValue("Unit Inputs")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_HOLD_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Hold Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_RESERVATION_AND_DEAL_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Reservation And Deal Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Reports")
                .labelValue("Market Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL_REPORT")
                .groupName("Reports")
                .labelValue("Channel Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Deals
        privileges.add(Privilege.builder()
                .name("DEALS")
                .groupName("Deals")
                .labelValue("Deals")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("PROJECTS")
                .groupName("Others")
                .labelValue("Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("REGIONS")
                .groupName("Others")
                .labelValue("Regions")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("AREA")
                .groupName("Others")
                .labelValue("Area")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MY_PROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("STAGE")
                .groupName("Others")
                .labelValue("Stage")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Landpage")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("USERS")
                .groupName("Others")
                .labelValue("Users")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LEAD_GENERATION")
                .groupName("Others")
                .labelValue("Lead Generation")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CAMPAIGN")
                .groupName("Others")
                .labelValue("Campaign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_ACTION_ON_TEAM_MEMBERS")
                .groupName("Others")
                .labelValue("Add Action On Team Members")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SEND_CIL")
                .groupName("Others")
                .labelValue("Send CIL")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CUSTOMER_SERVICES")
                .groupName("Others")
                .labelValue("Customer Services")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_CUSTOMERS")
                .groupName("Others")
                .labelValue("Assign Customers")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MULTI_ACTION")
                .groupName("Others")
                .labelValue("Multi Action")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("FILTER_BY_CHANNEL")
                .groupName("Controls")
                .labelValue("Filter By Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_TO_QUEUE")
                .groupName("Controls")
                .labelValue("Assign To Queue")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privilegeGroupRepository.save(privilegeGroup);
    }

    void teleSalesLeader() {
        // Create a list to hold the Privileges for the Telesales Leader role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Telesales Leader"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Telesales Leader")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Deals
        privileges.add(Privilege.builder()
                .name("DEALS")
                .groupName("Deals")
                .labelValue("Deals")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("MY_PROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_ACTION_ON_TEAM_MEMBERS")
                .groupName("Others")
                .labelValue("Add Action On Team Members")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_SELLER_CHART")
                .groupName("Others")
                .labelValue("Top Seller Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_ACTION_CHART")
                .groupName("Others")
                .labelValue("Top Action Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());


        privilegeGroupRepository.save(privilegeGroup);
    }

    void teleSalesAgent() {
        {
            // Create a list to hold the Privileges for the Telesales Agent role
            List<Privilege> privileges = new ArrayList<>();
            // Create and save the privilege group for "Telesales Agent"
            PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                    .jobName("Telesales Agent")
                    .privileges(privileges)
                    .build();
            // Leads
            privileges.add(Privilege.builder()
                    .name("ADD_CLIENT")
                    .groupName("Leads")
                    .labelValue("Add Client")
                    .defaultValue(true)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("SHOW_LEAD_CREATOR")
                    .groupName("Leads")
                    .labelValue("Show Lead Creator")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("DISABLE_EDIT_LEAD_PROFILE")
                    .groupName("Leads")
                    .labelValue("Disable Edit Lead Profile")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("EDIT_PHONE")
                    .groupName("Leads")
                    .labelValue("Edit Phone")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            // Units
            privileges.add(Privilege.builder()
                    .name("ADD_UNIT")
                    .groupName("Units")
                    .labelValue("Add Unit")
                    .defaultValue(true)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("UNIT_EXPORT")
                    .groupName("Units")
                    .labelValue("Unit Export")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            // Reports
            privileges.add(Privilege.builder()
                    .name("SALES_REPORTS")
                    .groupName("Reports")
                    .labelValue("Sales Reports")
                    .defaultValue(true)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("CALLLOG_REPORT")
                    .groupName("Reports")
                    .labelValue("Call Log Report")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            // Owners
            privileges.add(Privilege.builder()
                    .name("OWNER")
                    .groupName("Owners")
                    .labelValue("Owner")
                    .defaultValue(true)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("ALL_OWNERS")
                    .groupName("Owners")
                    .labelValue("All Owners")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            // Others
            privileges.add(Privilege.builder()
                    .name("MY_PROJECTS")
                    .groupName("Others")
                    .labelValue("My Projects")
                    .defaultValue(true)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("ALLOW_TEXT_COPY")
                    .groupName("Others")
                    .labelValue("Allow Text Copy")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("TOP_SELLER_CHART")
                    .groupName("Others")
                    .labelValue("Top Seller Chart")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("TOP_ACTION_CHART")
                    .groupName("Others")
                    .labelValue("Top Action Chart")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("DEVELOPER_COMPANY")
                    .groupName("Others")
                    .labelValue("Developer Company")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("SALES_FEEDBACK")
                    .groupName("Others")
                    .labelValue("Sales Feedback")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                    .groupName("Others")
                    .labelValue("Show Customer Service Comments")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("RESHUFFLE_COLD_LEADS")
                    .groupName("Others")
                    .labelValue("Reshuffle Cold Leads")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            // Controls
            privileges.add(Privilege.builder()
                    .name("SMS")
                    .groupName("Controls")
                    .labelValue("SMS")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("EMAILS")
                    .groupName("Controls")
                    .labelValue("Emails")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("FILTER_BY_CHANNEL")
                    .groupName("Controls")
                    .labelValue("Filter By Channel")
                    .defaultValue(true)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("PREVIEW_CHANNEL")
                    .groupName("Controls")
                    .labelValue("Preview Channel")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("EDIT_CHANNEL")
                    .groupName("Controls")
                    .labelValue("Edit Channel")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());


            privilegeGroupRepository.save(privilegeGroup);
        }
    }

    void customerServiceTeamLeader() {
        {
            // Create a list to hold the Privileges for the Customer Service Team Leader role
            List<Privilege> privileges = new ArrayList<>();
            // Create and save the privilege group for "Customer Service Team Leader"
            PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                    .jobName("Customer Service Team Leader")
                    .privileges(privileges)
                    .build();
            // Customers
            privileges.add(Privilege.builder()
                    .name("SHOW_CUSTOMER")
                    .groupName("Customers")
                    .labelValue("Show Customer")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("DISABLE_EDIT_CUSTOMER_PROFILE")
                    .groupName("Customers")
                    .labelValue("Disable Edit Customer Profile")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("EDIT_PHONE")
                    .groupName("Customers")
                    .labelValue("Edit Phone")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("ASSIGN_CUSTOMERS")
                    .groupName("Customers")
                    .labelValue("Assign Customers")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            // Others
            privileges.add(Privilege.builder()
                    .name("ALL_CUSTOMERS")
                    .groupName("Others")
                    .labelValue("All Customers")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("MY_CUSTOMERS")
                    .groupName("Others")
                    .labelValue("My Customers")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("ALLOW_TEXT_COPY")
                    .groupName("Others")
                    .labelValue("Allow Text Copy")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("COLLECTIONS")
                    .groupName("Others")
                    .labelValue("Collections")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("ACCOUNTANT_ACTIONS")
                    .groupName("Others")
                    .labelValue("Accountant Actions")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("QUALITY_COMMENTS")
                    .groupName("Others")
                    .labelValue("Quality Comments")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("SALES_FEEDBACK")
                    .groupName("Others")
                    .labelValue("Sales Feedback")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            // Controls
            privileges.add(Privilege.builder()
                    .name("ASSIGN")
                    .groupName("Controls")
                    .labelValue("Assign")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("EXPORT")
                    .groupName("Controls")
                    .labelValue("Export")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("SMS")
                    .groupName("Controls")
                    .labelValue("SMS")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());

            privileges.add(Privilege.builder()
                    .name("EMAILS")
                    .groupName("Controls")
                    .labelValue("Emails")
                    .defaultValue(false)
                    .privilegeGroup(privilegeGroup)
                    .build());


            privilegeGroupRepository.save(privilegeGroup);
        }
    }

    void branchManger() {
        // Create a list to hold the Privileges for the Branch Manager role
        List<Privilege> privileges = new ArrayList<>();
        // Create and save the privilege group for "Branch Manager"
        PrivilegeGroup privilegeGroup = PrivilegeGroup.builder()
                .jobName("Branch Manager")
                .privileges(privileges)
                .build();
        // Leads
        privileges.add(Privilege.builder()
                .name("ADD_CLIENT")
                .groupName("Leads")
                .labelValue("Add Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_CLIENT")
                .groupName("Leads")
                .labelValue("Import Client")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CLIENT_INPUT")
                .groupName("Leads")
                .labelValue("Client Input")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_LEAD_CREATOR")
                .groupName("Leads")
                .labelValue("Show Lead Creator")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DISABLE_EDIT_LEAD_PROFILE")
                .groupName("Leads")
                .labelValue("Disable Edit Lead Profile")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_PHONE")
                .groupName("Leads")
                .labelValue("Edit Phone")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LEAD_GENERATION")
                .groupName("Leads")
                .labelValue("Lead Generation")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Units
        privileges.add(Privilege.builder()
                .name("UNIT_INPUTS")
                .groupName("Units")
                .labelValue("Unit Inputs")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_UNIT")
                .groupName("Units")
                .labelValue("Add Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_UNIT")
                .groupName("Units")
                .labelValue("Edit Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("IMPORT_UNIT")
                .groupName("Units")
                .labelValue("Import Unit")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("UNIT_EXPORT")
                .groupName("Units")
                .labelValue("Unit Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_HOLD_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Hold Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ACCEPT_RESERVATION_AND_DEAL_REQUESTS")
                .groupName("Units")
                .labelValue("Accept Reservation and Deal Requests")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Reports
        privileges.add(Privilege.builder()
                .name("SALES_REPORTS")
                .groupName("Reports")
                .labelValue("Sales Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MARKET_REPORTS")
                .groupName("Reports")
                .labelValue("Market Reports")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL_REPORT")
                .groupName("Reports")
                .labelValue("Channel Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CALLLOG_REPORT")
                .groupName("Reports")
                .labelValue("Call Log Report")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Owners
        privileges.add(Privilege.builder()
                .name("OWNER")
                .groupName("Owners")
                .labelValue("Owner")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("OWNER_EXPORT")
                .groupName("Owners")
                .labelValue("Owner Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALL_OWNERS")
                .groupName("Owners")
                .labelValue("All Owners")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        // Deals
        privileges.add(Privilege.builder()
                .name("DEALS")
                .groupName("Deals")
                .labelValue("Deals")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEAL_REQUEST")
                .groupName("Deals")
                .labelValue("Deal Request")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Others
        privileges.add(Privilege.builder()
                .name("PROJECTS")
                .groupName("Others")
                .labelValue("Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("REGIONS")
                .groupName("Others")
                .labelValue("Regions")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("MY_PROJECTS")
                .groupName("Others")
                .labelValue("My Projects")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("STAGE")
                .groupName("Others")
                .labelValue("Stage")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CHANNEL")
                .groupName("Others")
                .labelValue("Channel")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("LANDPAGE")
                .groupName("Others")
                .labelValue("Land Page")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("USERS")
                .groupName("Others")
                .labelValue("Users")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("CAMPAIGN")
                .groupName("Others")
                .labelValue("Campaign")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ADD_ACTION_ON_TEAM_MEMBERS")
                .groupName("Others")
                .labelValue("Add Action on Team Members")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ALLOW_TEXT_COPY")
                .groupName("Others")
                .labelValue("Allow Text Copy")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SALES_FEEDBACK")
                .groupName("Others")
                .labelValue("Sales Feedback")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESERVATION_REQUEST")
                .groupName("Others")
                .labelValue("Reservation Request")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("COLLECTIONS")
                .groupName("Others")
                .labelValue("Collections")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_SELLER_CHART")
                .groupName("Others")
                .labelValue("Top Seller Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("TOP_ACTION_CHART")
                .groupName("Others")
                .labelValue("Top Action Chart")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("DEVELOPER_COMPANY")
                .groupName("Others")
                .labelValue("Developer Company")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("QUALITY_COMMENTS")
                .groupName("Others")
                .labelValue("Quality Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SHOW_CUSTOMER_SERVICE_COMMENTS")
                .groupName("Others")
                .labelValue("Show Customer Service Comments")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("RESHUFFLE_COLD_LEADS")
                .groupName("Others")
                .labelValue("Reshuffle Cold Leads")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        // Controls
        privileges.add(Privilege.builder()
                .name("ASSIGN")
                .groupName("Controls")
                .labelValue("Assign")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EXPORT")
                .groupName("Controls")
                .labelValue("Export")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("SMS")
                .groupName("Controls")
                .labelValue("SMS")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EMAILS")
                .groupName("Controls")
                .labelValue("Emails")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("FILTER_BY_CHANNEL")
                .groupName("Controls")
                .labelValue("Filter by Channel")
                .defaultValue(true)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("ASSIGN_TO_QUEUE")
                .groupName("Controls")
                .labelValue("Assign to Queue")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());

        privileges.add(Privilege.builder()
                .name("EDIT_CHANNEL")
                .groupName("Controls")
                .labelValue("Edit Channel")
                .defaultValue(false)
                .privilegeGroup(privilegeGroup)
                .build());
        privilegeGroupRepository.save(privilegeGroup);
    }
}