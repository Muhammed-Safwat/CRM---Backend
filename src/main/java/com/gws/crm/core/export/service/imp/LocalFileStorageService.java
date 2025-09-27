package com.gws.crm.core.export.service.imp;

import com.gws.crm.core.export.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@Slf4j
public class LocalFileStorageService implements FileStorageService {

    @Value("${app.file.upload.dir:uploads/exports}")
    private String uploadDir;

    @Value("${app.file.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public String store(byte[] data, String filename, String contentType) {
        try {
            log.info("Attempting to store file: {} with size: {} bytes", filename, data.length);

            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(filename);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            log.info("File stored successfully at: {}", filePath.toAbsolutePath());

            String publicUrl = baseUrl + "/api/exports/" + filename;
            log.info("Public URL for file: {}", publicUrl);

            return publicUrl;

        } catch (IOException e) {
            log.error("Failed to store file {}: {}", filename, e.getMessage(), e);
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }
}
