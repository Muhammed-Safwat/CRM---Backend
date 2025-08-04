package com.gws.crm.core.actions.controller;

import com.gws.crm.core.actions.dtos.UserActivityDTO;
import com.gws.crm.core.actions.service.ExcelReportService;
import com.gws.crm.core.actions.service.HtmlReportService;
import com.gws.crm.core.actions.service.UserActivityReportGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-activities")

public class UserActivityReportController {

    private final Map<String, UserActivityReportGenerator> generatorMap;
    @Autowired
    private ExcelReportService excelReportService;
    @Autowired
    private HtmlReportService reportService;

    public UserActivityReportController(
            @Qualifier("pdfbox") UserActivityReportGenerator pdfBoxService,
            @Qualifier("openpdf") UserActivityReportGenerator openPdfService) {
        this.generatorMap = Map.of(
                "pdfbox", pdfBoxService,
                "openpdf", openPdfService
        );
    }

    @GetMapping("/user-activity")
    public void downloadUserActivityReport(@RequestParam(defaultValue = "pdfbox") String format,
                                           HttpServletResponse response) {
        try {
            List<UserActivityDTO> timeline = Arrays.asList(
                    new UserActivityDTO("LOGIN", "User logged in", "2025-07-25 08:00"),
                    new UserActivityDTO("CREATE", "Created new lead", "2025-07-25 08:30"),
                    new UserActivityDTO("EDIT", "Updated notes", "2025-07-25 09:00"),
                    new UserActivityDTO("ASSIGN", "Assigned lead to John", "2025-07-25 09:30")
            );

            UserActivityReportGenerator generator = generatorMap.getOrDefault(format, generatorMap.get("pdfbox"));

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=user-activity-report.pdf");

            try (OutputStream outputStream = response.getOutputStream()) {
                generator.generatePdfReport(timeline, outputStream);
                outputStream.flush();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() {
        try {
            byte[] pdfBytes = reportService.generateReportPdf();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("user-report.pdf").build());
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/crm-excel")
    public ResponseEntity<byte[]> downloadCrmExcel() {
        try {
            byte[] excelBytes = excelReportService.generateCrmExcelReport();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=crm-report.xlsx")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
