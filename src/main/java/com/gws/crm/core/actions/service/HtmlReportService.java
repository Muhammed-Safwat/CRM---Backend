package com.gws.crm.core.actions.service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class HtmlReportService {


    public byte[] generateReportPdf() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("node", "src/main/resources/scripts/generate-pdf.js");

        Process process = pb.start();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        try (
                InputStream stdOut = process.getInputStream();
                InputStream stdErr = process.getErrorStream()
        ) {
            Thread outThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = stdOut.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                } catch (IOException ignored) {
                }
            });

            Thread errThread = new Thread(() -> {
                try {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = stdErr.read(buffer)) != -1) {
                        errorStream.write(buffer, 0, len);
                    }
                } catch (IOException ignored) {
                }
            });

            outThread.start();
            errThread.start();
            outThread.join();
            errThread.join();
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String errorOutput = errorStream.toString(StandardCharsets.UTF_8);
            System.err.println("[NodeJS Error Output] " + errorOutput); // useful in logs
            throw new IllegalStateException("PDF generation failed. Exit code: " + exitCode + ", Error: " + errorOutput);
        }

        return outputStream.toByteArray();
    }
}



