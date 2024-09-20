package com.gws.crm;

import com.gws.crm.authentication.repository.PrivilegeRepository;
import com.gws.crm.authentication.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @PostConstruct
    public void initializeData() {
        // Create Privileges
// Leads Privileges
       /* Privilege addClientPrivilege = new Privilege();
        addClientPrivilege.setName("ADD_CLIENT_PRIVILEGE");
        addClientPrivilege.setGroupName("Leads");
        addClientPrivilege.setLabelValue("Add Client Privilege");

        Privilege importClientPrivilege = new Privilege();
        importClientPrivilege.setName("IMPORT_CLIENT_PRIVILEGE");
        importClientPrivilege.setGroupName("Leads");
        importClientPrivilege.setLabelValue("Import Client Privilege");

        Privilege showLeadCreatorPrivilege = new Privilege();
        showLeadCreatorPrivilege.setName("SHOW_LEAD_CREATOR_PRIVILEGE");
        showLeadCreatorPrivilege.setGroupName("Leads");
        showLeadCreatorPrivilege.setLabelValue("Show Lead Creator Privilege");

        Privilege disableEditLeadPrivilege = new Privilege();
        disableEditLeadPrivilege.setName("DISABLE_EDIT_LEAD_PRIVILEGE");
        disableEditLeadPrivilege.setGroupName("Leads");
        disableEditLeadPrivilege.setLabelValue("Disable Edit Lead Privilege");

        Privilege editPhonePrivilege = new Privilege();
        editPhonePrivilege.setName("EDIT_PHONE_PRIVILEGE");
        editPhonePrivilege.setGroupName("Leads");
        editPhonePrivilege.setLabelValue("Edit Phone Privilege");

        Privilege leadGenerationProfilePrivilege = new Privilege();
        leadGenerationProfilePrivilege.setName("LEAD_GENERATION_PROFILE_PRIVILEGE");
        leadGenerationProfilePrivilege.setGroupName("Leads");
        leadGenerationProfilePrivilege.setLabelValue("Lead Generation Profile Privilege");

        // Owners Privilege
        Privilege ownersPrivilege = new Privilege();
        ownersPrivilege.setName("OWNERS_PRIVILEGE");
        ownersPrivilege.setGroupName("Owners");
        ownersPrivilege.setLabelValue("Owners Privilege");

        // Reports Privileges
        Privilege marketReportsPrivilege = new Privilege();
        marketReportsPrivilege.setName("MARKET_REPORTS_PRIVILEGE");
        marketReportsPrivilege.setGroupName("Reports");
        marketReportsPrivilege.setLabelValue("Market Reports Privilege");

        Privilege channelReportsPrivilege = new Privilege();
        channelReportsPrivilege.setName("CHANNEL_REPORTS_PRIVILEGE");
        channelReportsPrivilege.setGroupName("Reports");
        channelReportsPrivilege.setLabelValue("Channel Reports Privilege");

        Privilege callLogReportPrivilege = new Privilege();
        callLogReportPrivilege.setName("CALL_LOG_REPORT_PRIVILEGE");
        callLogReportPrivilege.setGroupName("Reports");
        callLogReportPrivilege.setLabelValue("Call Log Report Privilege");

        // Properties Privileges
        Privilege addPropertyPrivilege = new Privilege();
        addPropertyPrivilege.setName("ADD_PROPERTY_PRIVILEGE");
        addPropertyPrivilege.setGroupName("Properties");
        addPropertyPrivilege.setLabelValue("Add Property Privilege");

        Privilege importUnitPrivilege = new Privilege();
        importUnitPrivilege.setName("IMPORT_UNIT_PRIVILEGE");
        importUnitPrivilege.setGroupName("Properties");
        importUnitPrivilege.setLabelValue("Import Unit Privilege");

        Privilege unitExportPrivilege = new Privilege();
        unitExportPrivilege.setName("UNIT_EXPORT_PRIVILEGE");
        unitExportPrivilege.setGroupName("Properties");
        unitExportPrivilege.setLabelValue("Unit Export Privilege");

        // Others Privileges
        Privilege channelPrivilege = new Privilege();
        channelPrivilege.setName("CHANNEL_PRIVILEGE");
        channelPrivilege.setGroupName("Others");
        channelPrivilege.setLabelValue("Channel Privilege");

        Privilege landPagePrivilege = new Privilege();
        landPagePrivilege.setName("LAND_PAGE_PRIVILEGE");
        landPagePrivilege.setGroupName("Others");
        landPagePrivilege.setLabelValue("Land Page Privilege");

        Privilege ownerCampaignPrivilege = new Privilege();
        ownerCampaignPrivilege.setName("OWNER_CAMPAIGN_PRIVILEGE");
        ownerCampaignPrivilege.setGroupName("Others");
        ownerCampaignPrivilege.setLabelValue("Owner Campaign Privilege");

        Privilege allowTextCopyPrivilege = new Privilege();
        allowTextCopyPrivilege.setName("ALLOW_TEXT_COPY_PRIVILEGE");
        allowTextCopyPrivilege.setGroupName("Others");
        allowTextCopyPrivilege.setLabelValue("Allow Text Copy Privilege");

        Privilege developerCompanyPrivilege = new Privilege();
        developerCompanyPrivilege.setName("DEVELOPER_COMPANY_PRIVILEGE");
        developerCompanyPrivilege.setGroupName("Others");
        developerCompanyPrivilege.setLabelValue("Developer Company Privilege");

        Privilege salesFeedbackPrivilege = new Privilege();
        salesFeedbackPrivilege.setName("SALES_FEEDBACK_PRIVILEGE");
        salesFeedbackPrivilege.setGroupName("Others");
        salesFeedbackPrivilege.setLabelValue("Sales Feedback Privilege");

        Privilege qualityCommentsPrivilege = new Privilege();
        qualityCommentsPrivilege.setName("QUALITY_COMMENTS_PRIVILEGE");
        qualityCommentsPrivilege.setGroupName("Others");
        qualityCommentsPrivilege.setLabelValue("Quality Comments Privilege");

        // Save Privileges to the database
        privilegeRepository.saveAll(List.of(
                addClientPrivilege,
                importClientPrivilege,
                showLeadCreatorPrivilege,
                disableEditLeadPrivilege,
                editPhonePrivilege,
                leadGenerationProfilePrivilege,
                ownersPrivilege,
                marketReportsPrivilege,
                channelReportsPrivilege,
                callLogReportPrivilege,
                addPropertyPrivilege,
                importUnitPrivilege,
                unitExportPrivilege,
                channelPrivilege,
                landPagePrivilege,
                ownerCampaignPrivilege,
                allowTextCopyPrivilege,
                developerCompanyPrivilege,
                salesFeedbackPrivilege,
                qualityCommentsPrivilege
        ));
        Role adminRole = new Role();
        adminRole.setName("ADMIN_ROLE");
        Role userRole = new Role();
        userRole.setName("USER_ROLE");

        roleRepository.saveAll(List.of(adminRole,userRole));*/

/*
// Create EMPLOYEE_ROLE
        Role employeeRole = new Role();
        employeeRole.setName("EMPLOYEE_ROLE");

// Assign Privileges to EMPLOYEE_ROLE
        employeeRole.setPrivileges(Set.of(
                addClientPrivilege,
                importClientPrivilege,
                showLeadCreatorPrivilege,
                disableEditLeadPrivilege,
                editPhonePrivilege,
                leadGenerationProfilePrivilege,
                ownersPrivilege,
                marketReportsPrivilege,
                channelReportsPrivilege,
                callLogReportPrivilege,
                addPropertyPrivilege,
                importUnitPrivilege,
                unitExportPrivilege,
                channelPrivilege,
                landPagePrivilege,
                ownerCampaignPrivilege,
                allowTextCopyPrivilege,
                developerCompanyPrivilege,
                salesFeedbackPrivilege,
                qualityCommentsPrivilege
        ));

// Save EMPLOYEE_ROLE
        roleRepository.save(employeeRole);

        // Generate a secure random key
        // Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Print the key in Base64 format
        // log.info("Secret Key (Base64): " +  Encoders.BASE64.encode(key.getEncoded()));
         // Create Privileges

        // Save Privileges

        // Create Roles
        Role adminRole = new Role();
        adminRole.setName("ADMIN_ROLE");
        Role userRole = new Role();
        userRole.setName("USER_ROLE");

        // Assign Privileges to Roles
        adminRole.setPrivileges(Set.of(
                addClientPrivilege,
                importClientPrivilege,
                showLeadCreatorPrivilege,
                disableEditLeadPrivilege,
                editPhonePrivilege,
                leadGenerationProfilePrivilege,
                ownersPrivilege,
                marketReportsPrivilege,
                channelReportsPrivilege,
                callLogReportPrivilege,
                addPropertyPrivilege,
                importUnitPrivilege,
                unitExportPrivilege,
                channelPrivilege,
                landPagePrivilege,
                ownerCampaignPrivilege,
                allowTextCopyPrivilege,
                developerCompanyPrivilege,
                salesFeedbackPrivilege,
                qualityCommentsPrivilege
        ));
        /*
        userRole.setPrivileges(Set.of(
                addClientPrivilege,
                importClientPrivilege,
                showLeadCreatorPrivilege,
                disableEditLeadPrivilege,
                editPhonePrivilege,
                leadGenerationProfilePrivilege,
                ownersPrivilege,
                marketReportsPrivilege,
                channelReportsPrivilege,
                callLogReportPrivilege,
                addPropertyPrivilege,
                importUnitPrivilege,
                unitExportPrivilege,
                channelPrivilege,
                landPagePrivilege,
                ownerCampaignPrivilege,
                allowTextCopyPrivilege,
                developerCompanyPrivilege,
                salesFeedbackPrivilege,
                qualityCommentsPrivilege
        ));
        // Save Roles
        roleRepository.saveAll(List.of(adminRole, userRole));
    }*/
    }

}