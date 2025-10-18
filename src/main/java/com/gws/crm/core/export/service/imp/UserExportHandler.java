package com.gws.crm.core.export.service.imp;

import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.core.employee.entity.Employee;
import com.gws.crm.core.employee.repository.EmployeeRepository;
import com.gws.crm.core.export.service.ExportHandler;
import lombok.RequiredArgsConstructor;
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
public class UserExportHandler implements ExportHandler {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean supports(String referenceType) {
        return "USER".equalsIgnoreCase(referenceType);
    }

    @Override
    public byte[] generateFile(List<Long> ids, Map<String, Object> params) throws Exception { 
        List<User> users = userRepository.findAllById(ids);
        List<Employee> employees = employeeRepository.findAllById(ids);

        try (Workbook workbook = new XSSFWorkbook()) { 
            Map<String, CellStyle> styles = createStyles(workbook);
 
            createUsersSheet(workbook, users, styles);
 
            createEmployeesSheet(workbook, employees, styles);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        }
    }

    private Map<String, CellStyle> createStyles(Workbook workbook) {
        // Title Style
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

        // Header Style
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

        // Data Style
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

        return Map.of(
                "title", titleStyle,
                "header", headerStyle,
                "data", dataStyle);
    }

    private void createUsersSheet(Workbook workbook, List<User> users, Map<String, CellStyle> styles) {
        Sheet sheet = workbook.createSheet("👥 Users");

        // Title Row
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("👥 Users Export Report");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Summary Row
        Row summaryRow = sheet.createRow(1);
        Cell summaryCell = summaryRow.createCell(0);
        summaryCell.setCellValue(
                "📊 Total Users: " + users.size() + " | Generated: " + java.time.LocalDateTime.now().format(DATE_FORMATTER));
        summaryCell.setCellStyle(styles.get("data"));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

        // Empty row
        sheet.createRow(2);

        // Headers
        Row headerRow = sheet.createRow(3);
        String[] columns = {"🆔 ID", "👤 Name", "📧 Username", "📞 Phone", "📅 Created At", "🔒 Enabled", "🔓 Locked", "🗑️ Deleted"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(styles.get("header"));
        }

        // Data rows
        int rowIdx = 4;
        for (User user : users) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getName() != null ? user.getName() : "N/A");
            row.createCell(2).setCellValue(user.getUsername() != null ? user.getUsername() : "N/A");
            row.createCell(3).setCellValue(user.getPhone() != null ? user.getPhone() : "N/A");
            row.createCell(4).setCellValue(user.getCreatedAt() != null ? user.getCreatedAt().format(DATE_FORMATTER) : "N/A");
            row.createCell(5).setCellValue(user.isEnabled() ? "Yes" : "No");
            row.createCell(6).setCellValue(user.isAccountNonLocked() ? "No" : "Yes");
            row.createCell(7).setCellValue(user.isDeleted() ? "Yes" : "No");

            // Apply data style to all cells
            for (int i = 0; i < 8; i++) {
                row.getCell(i).setCellStyle(styles.get("data"));
            }
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createEmployeesSheet(Workbook workbook, List<Employee> employees, Map<String, CellStyle> styles) {
        Sheet sheet = workbook.createSheet("👨‍💼 Employees");

        // Title Row
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("👨‍💼 Employees Export Report");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        // Summary Row
        Row summaryRow = sheet.createRow(1);
        Cell summaryCell = summaryRow.createCell(0);
        summaryCell.setCellValue(
                "📊 Total Employees: " + employees.size() + " | Generated: " + java.time.LocalDateTime.now().format(DATE_FORMATTER));
        summaryCell.setCellStyle(styles.get("data"));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));

        // Empty row
        sheet.createRow(2);

        // Headers
        Row headerRow = sheet.createRow(3);
        String[] columns = {"🆔 ID", "👤 Name", "📧 Username", "💼 Job Name", "📞 Phone", "📅 Created At", "🔒 Enabled"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(styles.get("header"));
        }

        // Data rows
        int rowIdx = 4;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(employee.getId());
            row.createCell(1).setCellValue(employee.getName() != null ? employee.getName() : "N/A");
            row.createCell(2).setCellValue(employee.getUsername() != null ? employee.getUsername() : "N/A");
            row.createCell(3).setCellValue(employee.getJobName() != null ? employee.getJobName() : "N/A");
            row.createCell(4).setCellValue(employee.getPhone() != null ? employee.getPhone() : "N/A");
            row.createCell(5).setCellValue(employee.getCreatedAt() != null ? employee.getCreatedAt().format(DATE_FORMATTER) : "N/A");
            row.createCell(6).setCellValue(employee.isEnabled() ? "Yes" : "No");

            // Apply data style to all cells
            for (int i = 0; i < 7; i++) {
                row.getCell(i).setCellStyle(styles.get("data"));
            }
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    public String defaultFilename() {
        return "users_employees_export.xlsx";
    }
}
