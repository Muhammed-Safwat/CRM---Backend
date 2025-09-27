package com.gws.crm.core.export.service.imp;

import com.gws.crm.core.export.service.ExportHandler;
import com.gws.crm.core.leads.entity.BaseLead;
import com.gws.crm.core.leads.entity.Lead;
import com.gws.crm.core.leads.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LeadExportHandler implements ExportHandler {

    private final LeadRepository leadRepository;

    @Override
    public boolean supports(String referenceType) {
        return "LEAD".equalsIgnoreCase(referenceType);
    }

    @Override
    public byte[] generateFile(List<Long> ids, Map<String, Object> params) throws Exception {
          List<Lead> leads = leadRepository.findAllBasic(ids);

        leadRepository.findAllWithPhones(ids);

        leadRepository.findAllWithActions(ids);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet dashboard = workbook.createSheet("Dashboard");

            //   Styling
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            //   Dashboard Header
            Row header = dashboard.createRow(0);
            String[] columns = {"ID", "Name", "Email", "Phone", "Status", "Project", "Next Action", "Details"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (BaseLead lead : leads) {
                Row row = dashboard.createRow(rowIdx++);
                row.createCell(0).setCellValue(lead.getId());
                row.createCell(1).setCellValue(lead.getName());
                row.createCell(2).setCellValue(lead.getEmail() != null ? lead.getEmail() : "");
             /*   row.createCell(3).setCellValue(lead.getPhoneNumbers() != null && !lead.getPhoneNumbers().isEmpty()
                        ? lead.getPhoneNumbers().get(0).getNumber()
                        : "");*/
                row.createCell(4).setCellValue(lead instanceof com.gws.crm.core.leads.entity.SalesLead sales
                        ? sales.getStatus().getName()
                        : "");
                row.createCell(5).setCellValue(lead.getProject() != null ? lead.getProject().getName() : "");
                row.createCell(6).setCellValue(lead.getNextActionDate() != null ? lead.getNextActionDate().toString() : "");

                //   Create detail sheet per lead
                String sheetName = "Lead-" + lead.getId();
                Sheet detailSheet = workbook.createSheet(sheetName);
                buildLeadDetailSheet(detailSheet, lead, headerStyle);

                //   Hyperlink from Dashboard
                CreationHelper createHelper = workbook.getCreationHelper();
                Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
                link.setAddress("'" + sheetName + "'!A1");
                Cell linkCell = row.createCell(7);
                linkCell.setCellValue("View Details");
                linkCell.setHyperlink(link);
                linkCell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < columns.length; i++) {
                dashboard.autoSizeColumn(i);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        }
    }

    private void buildLeadDetailSheet(Sheet sheet, BaseLead lead, CellStyle headerStyle) {
        int rowIdx = 0;

        //   Basic Info
        Row header = sheet.createRow(rowIdx++);
        header.createCell(0).setCellValue("Field");
        header.createCell(1).setCellValue("Value");
        header.getCell(0).setCellStyle(headerStyle);
        header.getCell(1).setCellStyle(headerStyle);

        sheet.createRow(rowIdx++).createCell(0).setCellValue("Name");
        sheet.getRow(rowIdx - 1).createCell(1).setCellValue(lead.getName());

        sheet.createRow(rowIdx++).createCell(0).setCellValue("Email");
        sheet.getRow(rowIdx - 1).createCell(1).setCellValue(lead.getEmail() != null ? lead.getEmail() : "");

        sheet.createRow(rowIdx++).createCell(0).setCellValue("Country");
        sheet.getRow(rowIdx - 1).createCell(1).setCellValue(lead.getCountry() != null ? lead.getCountry() : "");

        sheet.createRow(rowIdx++).createCell(0).setCellValue("Project");
        sheet.getRow(rowIdx - 1).createCell(1).setCellValue(lead.getProject() != null ? lead.getProject().getName() : "");

        //   Comments Table
        rowIdx++;
        Row cHeader = sheet.createRow(rowIdx++);
        cHeader.createCell(0).setCellValue("Comments");
        cHeader.getCell(0).setCellStyle(headerStyle);

        // هنا ممكن تحط loop على lead.getActions() وتجيب الـ comments
        // مثال:
        // for (UserAction action : lead.getActions()) { ... }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    @Override
    public String defaultFilename() {
        return "leads_export.xlsx";
    }
}
