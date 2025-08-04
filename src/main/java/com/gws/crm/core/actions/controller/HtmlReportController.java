package com.gws.crm.core.actions.controller;

import com.gws.crm.core.actions.service.HtmlReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class HtmlReportController {

    private final HtmlReportService reportService;

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

}
