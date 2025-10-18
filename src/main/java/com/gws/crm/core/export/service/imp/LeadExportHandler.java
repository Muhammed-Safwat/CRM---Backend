package com.gws.crm.core.export.service.imp;

import com.gws.crm.core.export.service.ExportHandler;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.entity.PhoneNumber;
import com.gws.crm.core.leads.repository.LeadRepository;
import com.gws.crm.core.actions.entity.UserAction;
import lombok.RequiredArgsConstructor;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LeadExportHandler implements ExportHandler {

  private final LeadRepository leadRepository;
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public boolean supports(String referenceType) {
    return "LEAD".equalsIgnoreCase(referenceType);
  }

  @Override
  public byte[] generateFile(List<Long> ids, Map<String, Object> params) throws Exception {
    List<Lead> leads = leadRepository.findAllWithAllRelations(ids);

    try (Workbook workbook = new XSSFWorkbook()) {
      // Create styles
      Map<String, CellStyle> styles = createStyles(workbook);

      // Create Dashboard Sheet
      createDashboardSheet(workbook, leads, styles);

      // Create detail sheets for each lead
      for (BaseLead lead : leads) {
        createLeadDetailSheet(workbook, lead, styles);
      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      workbook.write(bos);
      return bos.toByteArray();
    }
  }

  private Map<String, CellStyle> createStyles(Workbook workbook) {
    // Title Style - Modern gradient-like effect
    CellStyle titleStyle = workbook.createCellStyle();
    Font titleFont = workbook.createFont();
    titleFont.setBold(true);
    titleFont.setFontHeightInPoints((short) 18);
    titleFont.setColor(IndexedColors.WHITE.getIndex());
    titleFont.setFontName("Segoe UI");
    titleStyle.setFont(titleFont);
    titleStyle.setAlignment(HorizontalAlignment.CENTER);
    titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    titleStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
    titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    // Header Style - Modern dark theme
    CellStyle headerStyle = workbook.createCellStyle();
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerFont.setFontHeightInPoints((short) 13);
    headerFont.setColor(IndexedColors.WHITE.getIndex());
    headerFont.setFontName("Segoe UI");
    headerStyle.setFont(headerFont);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);
    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerStyle.setBorderTop(BorderStyle.MEDIUM);
    headerStyle.setBorderBottom(BorderStyle.MEDIUM);
    headerStyle.setBorderLeft(BorderStyle.MEDIUM);
    headerStyle.setBorderRight(BorderStyle.MEDIUM);

    // Data Style - Modern clean look
    CellStyle dataStyle = workbook.createCellStyle();
    Font dataFont = workbook.createFont();
    dataFont.setFontHeightInPoints((short) 12);
    dataFont.setFontName("Segoe UI");
    dataStyle.setFont(dataFont);
    dataStyle.setAlignment(HorizontalAlignment.LEFT);
    dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    dataStyle.setBorderTop(BorderStyle.THIN);
    dataStyle.setBorderBottom(BorderStyle.THIN);
    dataStyle.setBorderLeft(BorderStyle.THIN);
    dataStyle.setBorderRight(BorderStyle.THIN);
    dataStyle.setWrapText(true);
    dataStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
    dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    // Link Style - Modern blue
    CellStyle linkStyle = workbook.createCellStyle();
    Font linkFont = workbook.createFont();
    linkFont.setColor(IndexedColors.BLUE.getIndex());
    linkFont.setUnderline(Font.U_SINGLE);
    linkFont.setFontName("Segoe UI");
    linkFont.setFontHeightInPoints((short) 12);
    linkStyle.setFont(linkFont);
    linkStyle.setAlignment(HorizontalAlignment.CENTER);
    linkStyle.setVerticalAlignment(VerticalAlignment.CENTER);

    // Section Header Style - Modern accent color
    CellStyle sectionHeaderStyle = workbook.createCellStyle();
    Font sectionFont = workbook.createFont();
    sectionFont.setBold(true);
    sectionFont.setFontHeightInPoints((short) 14);
    sectionFont.setColor(IndexedColors.WHITE.getIndex());
    sectionFont.setFontName("Segoe UI");
    sectionHeaderStyle.setFont(sectionFont);
    sectionHeaderStyle.setFillForegroundColor(IndexedColors.TEAL.getIndex());
    sectionHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    sectionHeaderStyle.setAlignment(HorizontalAlignment.LEFT);
    sectionHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);

    return Map.of(
        "title", titleStyle,
        "header", headerStyle,
        "data", dataStyle,
        "link", linkStyle,
        "sectionHeader", sectionHeaderStyle);
  }

  private void createDashboardSheet(Workbook workbook, List<Lead> leads, Map<String, CellStyle> styles) {
    Sheet dashboard = workbook.createSheet("📊 Dashboard");

    // Title Row
    Row titleRow = dashboard.createRow(0);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue("🏢 CRM Leads Export Report");
    titleCell.setCellStyle(styles.get("title"));
    dashboard.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

    // Summary Row
    Row summaryRow = dashboard.createRow(1);
    Cell summaryCell = summaryRow.createCell(0);
    summaryCell.setCellValue(
        "📈 Total Leads: " + leads.size() + " | Generated: " + java.time.LocalDateTime.now().format(DATE_FORMATTER));
    summaryCell.setCellStyle(styles.get("data"));
    dashboard.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

    // Empty row
    dashboard.createRow(2);

    // Headers
    Row headerRow = dashboard.createRow(3);
    String[] columns = { "🆔 ID", "👤 Name", "📧 Email", "📞 Phone", "📊 Status", "🏗️ Project", "📅 Next Action",
        "🔗 Details", "📝 Actions" };

    for (int i = 0; i < columns.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(columns[i]);
      cell.setCellStyle(styles.get("header"));
    }

    // Data rows
    int rowIdx = 4;
    for (BaseLead lead : leads) {
      Row row = dashboard.createRow(rowIdx++);

      // ID
      row.createCell(0).setCellValue(lead.getId());

      // Name
      row.createCell(1).setCellValue(lead.getName());

      // Email
      row.createCell(2).setCellValue(lead.getEmail() != null ? lead.getEmail() : "N/A");

      // Phone
      /*
       * String phoneNumbers = getPhoneNumbers(lead);
       * row.createCell(3).setCellValue(phoneNumbers);
       * 
       */

      // Status
      String status = getLeadStatus(lead);
      row.createCell(4).setCellValue(status);

      // Project
      row.createCell(5).setCellValue(lead.getProject() != null ? lead.getProject().getName() : "N/A");

      // Next Action
      row.createCell(6)
          .setCellValue(lead.getNextActionDate() != null ? lead.getNextActionDate().format(DATE_FORMATTER) : "N/A");

      // Details Link
      String sheetName = "Lead-" + lead.getId();
      CreationHelper createHelper = workbook.getCreationHelper();
      Hyperlink detailsLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
      detailsLink.setAddress("'" + sheetName + "'!A1");
      Cell detailsCell = row.createCell(7);
      detailsCell.setCellValue("👁️ View Details");
      detailsCell.setHyperlink(detailsLink);
      detailsCell.setCellStyle(styles.get("link"));

      // Actions Link
      Hyperlink actionsLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
      actionsLink.setAddress("'" + sheetName + "'!A50");
      Cell actionsCell = row.createCell(8);
      actionsCell.setCellValue("📝 View Actions");
      actionsCell.setHyperlink(actionsLink);
      actionsCell.setCellStyle(styles.get("link"));
    }

    // Auto-size columns
    for (int i = 0; i < columns.length; i++) {
      dashboard.autoSizeColumn(i);
    }
  }

  private void createLeadDetailSheet(Workbook workbook, BaseLead lead, Map<String, CellStyle> styles) {
    String sheetName = "Lead-" + lead.getId();
    Sheet sheet = workbook.createSheet(sheetName);

    int rowIdx = 0;

    // Title
    Row titleRow = sheet.createRow(rowIdx++);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue("👤 Lead Details - " + lead.getName() + " (ID: " + lead.getId() + ")");
    titleCell.setCellStyle(styles.get("title"));
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

    // Back to Dashboard Link
    Row backRow = sheet.createRow(rowIdx++);
    Cell backCell = backRow.createCell(0);
    backCell.setCellValue("🔙 Back to Dashboard");
    CreationHelper createHelper = workbook.getCreationHelper();
    Hyperlink backLink = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
    backLink.setAddress("'📊 Dashboard'!A1");
    backCell.setHyperlink(backLink);
    backCell.setCellStyle(styles.get("link"));

    rowIdx++; // Empty row

    // Basic Information Section
    addSectionHeader(sheet, rowIdx++, "📋 Basic Information", styles.get("sectionHeader"));
    rowIdx = addBasicInfo(sheet, rowIdx, lead, styles.get("data"));

    rowIdx++; // Empty row

    // Contact Information Section
    addSectionHeader(sheet, rowIdx++, "📞 Contact Information", styles.get("sectionHeader"));
    rowIdx = addContactInfo(sheet, rowIdx, lead, styles.get("data"));

    rowIdx++; // Empty row

    // Business Information Section
    addSectionHeader(sheet, rowIdx++, "💼 Business Information", styles.get("sectionHeader"));
    rowIdx = addBusinessInfo(sheet, rowIdx, lead, styles.get("data"));

    rowIdx++; // Empty row

    // Status & Timeline Section
    addSectionHeader(sheet, rowIdx++, "📊 Status & Timeline", styles.get("sectionHeader"));
    rowIdx = addStatusInfo(sheet, rowIdx, lead, styles.get("data"));

    rowIdx++; // Empty row

    // Actions & Comments Section
    addSectionHeader(sheet, rowIdx++, "📝 Actions & Comments", styles.get("sectionHeader"));
    rowIdx = addActionsInfo(sheet, rowIdx, lead, styles.get("data"));

    // Auto-size columns
    for (int i = 0; i < 4; i++) {
      sheet.autoSizeColumn(i);
    }
  }

  private void addSectionHeader(Sheet sheet, int rowIdx, String title, CellStyle style) {
    Row row = sheet.createRow(rowIdx);
    Cell cell = row.createCell(0);
    cell.setCellValue(title);
    cell.setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 3));
  }

  private int addBasicInfo(Sheet sheet, int startRow, BaseLead lead, CellStyle dataStyle) {
    int rowIdx = startRow;

    addInfoRow(sheet, rowIdx++, "🆔 Lead ID", String.valueOf(lead.getId()), dataStyle);
    addInfoRow(sheet, rowIdx++, "👤 Name", lead.getName(), dataStyle);
    addInfoRow(sheet, rowIdx++, "📧 Email", lead.getEmail() != null ? lead.getEmail() : "N/A", dataStyle);
    addInfoRow(sheet, rowIdx++, "🌍 Country", lead.getCountry() != null ? lead.getCountry() : "N/A", dataStyle);
    addInfoRow(sheet, rowIdx++, "📝 Note", lead.getNote() != null ? lead.getNote() : "N/A", dataStyle);
    addInfoRow(sheet, rowIdx++, "📅 Created At",
        lead.getCreatedAt() != null ? lead.getCreatedAt().format(DATE_FORMATTER) : "N/A", dataStyle);
    addInfoRow(sheet, rowIdx++, "🔄 Updated At",
        lead.getUpdatedAt() != null ? lead.getUpdatedAt().format(DATE_FORMATTER) : "N/A", dataStyle);

    return rowIdx;
  }

  private int addContactInfo(Sheet sheet, int startRow, BaseLead lead, CellStyle dataStyle) {
    int rowIdx = startRow;
    /*
     * // Phone Numbers
     * String phoneNumbers = getPhoneNumbers(lead);
     * addInfoRow(sheet, rowIdx++, "📞 Phone Numbers", phoneNumbers, dataStyle);
     */
    if (lead instanceof com.gws.crm.core.leads.entity.SalesLead salesLead) {
      addInfoRow(sheet, rowIdx++, "📱 WhatsApp",
          salesLead.getWhatsappNumber() != null ? salesLead.getWhatsappNumber() : "N/A", dataStyle);
      addInfoRow(sheet, rowIdx++, "⏰ Contact Time",
          salesLead.getContactTime() != null ? salesLead.getContactTime() : "N/A", dataStyle);
    }

    return rowIdx;
  }

  private int addBusinessInfo(Sheet sheet, int startRow, BaseLead lead, CellStyle dataStyle) {
    int rowIdx = startRow;

    addInfoRow(sheet, rowIdx++, "🏗️ Project", lead.getProject() != null ? lead.getProject().getName() : "N/A",
        dataStyle);
    addInfoRow(sheet, rowIdx++, "📺 Channel", lead.getChannel() != null ? lead.getChannel().getName() : "N/A",
        dataStyle);
    addInfoRow(sheet, rowIdx++, "👨‍💼 Creator", lead.getCreator() != null ? lead.getCreator().getUsername() : "N/A",
        dataStyle);
    addInfoRow(sheet, rowIdx++, "👨‍💻 Admin", lead.getAdmin() != null ? lead.getAdmin().getUsername() : "N/A",
        dataStyle);

    if (lead instanceof com.gws.crm.core.leads.entity.SalesLead salesLead) {
      addInfoRow(sheet, rowIdx++, "💰 Budget", salesLead.getBudget() != null ? salesLead.getBudget() : "N/A",
          dataStyle);
      addInfoRow(sheet, rowIdx++, "💼 Job Title", salesLead.getJobTitle() != null ? salesLead.getJobTitle() : "N/A",
          dataStyle);
      addInfoRow(sheet, rowIdx++, "🎯 Investment Goal",
          salesLead.getInvestmentGoal() != null ? salesLead.getInvestmentGoal().getName() : "N/A", dataStyle);
      addInfoRow(sheet, rowIdx++, "📞 Communication Way",
          salesLead.getCommunicateWay() != null ? salesLead.getCommunicateWay().getName() : "N/A", dataStyle);
      addInfoRow(sheet, rowIdx++, "👨‍💼 Sales Rep",
          salesLead.getSalesRep() != null ? salesLead.getSalesRep().getUsername() : "N/A", dataStyle);
      addInfoRow(sheet, rowIdx++, "🏢 Broker", salesLead.getBroker() != null ? salesLead.getBroker().getName() : "N/A",
          dataStyle);
      addInfoRow(sheet, rowIdx++, "📈 Stage", salesLead.getStage() != null ? salesLead.getStage().getName() : "N/A",
          dataStyle);
      addInfoRow(sheet, rowIdx++, "🆔 Campaign ID",
          salesLead.getCampaignId() != null ? salesLead.getCampaignId() : "N/A", dataStyle);
      addInfoRow(sheet, rowIdx++, "📊 Last Stage", salesLead.getLastStage() != null ? salesLead.getLastStage() : "N/A",
          dataStyle);
    }

    return rowIdx;
  }

  private int addStatusInfo(Sheet sheet, int startRow, BaseLead lead, CellStyle dataStyle) {
    int rowIdx = startRow;

    String status = getLeadStatus(lead);
    addInfoRow(sheet, rowIdx++, "📊 Status", status, dataStyle);
    addInfoRow(sheet, rowIdx++, "⏰ Last Action Date",
        lead.getLastActionDate() != null ? lead.getLastActionDate().format(DATE_FORMATTER) : "N/A", dataStyle);
    addInfoRow(sheet, rowIdx++, "📅 Next Action Date",
        lead.getNextActionDate() != null ? lead.getNextActionDate().format(DATE_FORMATTER) : "N/A", dataStyle);
    addInfoRow(sheet, rowIdx++, "💬 Last Action Comment",
        lead.getLastActionComment() != null ? lead.getLastActionComment() : "N/A", dataStyle);
    addInfoRow(sheet, rowIdx++, "⏳ Delayed", lead.isDelay() ? "Yes" : "No", dataStyle);
    addInfoRow(sheet, rowIdx++, "✅ Reviewed by Sales", lead.isReviewedBySales() ? "Yes" : "No", dataStyle);
    addInfoRow(sheet, rowIdx++, "📁 Archived", lead.isArchive() ? "Yes" : "No", dataStyle);
    addInfoRow(sheet, rowIdx++, "🗑️ Deleted", lead.isDeleted() ? "Yes" : "No", dataStyle);

    if (lead instanceof com.gws.crm.core.leads.entity.SalesLead salesLead) {
      addInfoRow(sheet, rowIdx++, "📅 Assigned At",
          salesLead.getAssignAt() != null ? salesLead.getAssignAt().format(DATE_FORMATTER) : "N/A", dataStyle);
      addInfoRow(sheet, rowIdx++, "👨‍💼 Assigned From",
          salesLead.getAssignFrom() != null ? salesLead.getAssignFrom().getUsername() : "N/A", dataStyle);
      addInfoRow(sheet, rowIdx++, "❌ Cancel Reason",
          salesLead.getCancelReasons() != null ? salesLead.getCancelReasons().getName() : "N/A", dataStyle);
    }

    return rowIdx;
  }

  private int addActionsInfo(Sheet sheet, int startRow, BaseLead lead, CellStyle dataStyle) {
    int rowIdx = startRow;

    if (lead.getActions() != null && !lead.getActions().isEmpty()) {
      // Actions Table Header
      Row headerRow = sheet.createRow(rowIdx++);
      headerRow.createCell(0).setCellValue("📅 Date");
      headerRow.createCell(1).setCellValue("👤 User");
      headerRow.createCell(2).setCellValue("🔧 Action");
      headerRow.createCell(3).setCellValue("💬 Comment");

      // Style header cells
      for (int i = 0; i < 4; i++) {
        Cell cell = headerRow.getCell(i);
        cell.setCellStyle(dataStyle);
      }

      // Actions Data
      for (UserAction action : lead.getActions()) {
        Row actionRow = sheet.createRow(rowIdx++);
        actionRow.createCell(0)
            .setCellValue(action.getCreatedAt() != null ? action.getCreatedAt().format(DATE_FORMATTER) : "N/A");
        actionRow.createCell(1).setCellValue(action.getCreator() != null ? action.getCreator().getUsername() : "N/A");
        actionRow.createCell(2).setCellValue(action.getType() != null ? action.getType().getDisplayValue() : "N/A");

        String comment = "N/A";
        if (action.getLeadDetails() != null && action.getLeadDetails().getComment() != null) {
          comment = action.getLeadDetails().getComment();
        } else if (action.getDescription() != null) {
          comment = action.getDescription();
        }
        actionRow.createCell(3).setCellValue(comment);

        // Style data cells
        for (int i = 0; i < 4; i++) {
          Cell cell = actionRow.getCell(i);
          cell.setCellStyle(dataStyle);
        }
      }
    } else {
      addInfoRow(sheet, rowIdx++, "📝 Actions", "No actions recorded", dataStyle);
    }

    return rowIdx;
  }

  private void addInfoRow(Sheet sheet, int rowIdx, String label, String value, CellStyle dataStyle) {
    Row row = sheet.createRow(rowIdx);
    Cell labelCell = row.createCell(0);
    Cell valueCell = row.createCell(1);

    labelCell.setCellValue(label);
    valueCell.setCellValue(value);

    labelCell.setCellStyle(dataStyle);
    valueCell.setCellStyle(dataStyle);
  }

  /*
   * private String getPhoneNumbers(BaseLead lead) {
   * if (lead.getPhoneNumbers() != null && !lead.getPhoneNumbers().isEmpty()) {
   * return lead.getPhoneNumbers().stream()
   * .map(PhoneNumber::getNumber)
   * .reduce((a, b) -> a + ", " + b)
   * .orElse("N/A");
   * }
   * return "N/A";
   * }
   */
  private String getLeadStatus(BaseLead lead) {
    if (lead instanceof com.gws.crm.core.leads.entity.SalesLead salesLead) {
      return salesLead.getStatus() != null ? salesLead.getStatus().getName() : "N/A";
    }
    return "N/A";
  }

  @Override
  public String defaultFilename() {
    return "leads_export.xlsx";
  }
}
