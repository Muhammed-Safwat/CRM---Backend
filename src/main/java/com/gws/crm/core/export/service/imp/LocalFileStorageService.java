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

    @Override
    public String store(byte[] data, String filename, String contentType) {
        try {
            log.info("Attempting to store file: {} with size: {} bytes", filename, data.length);

            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(filename);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            log.info("File stored successfully at: {}", filePath.toAbsolutePath());

            // Return the filename as-is since it already contains the full relative path
            log.info("Stored file path: {}", filename);

            return filename;

        } catch (IOException e) {
            log.error("Failed to store file {}: {}", filename, e.getMessage(), e);
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] load(String path) {
        try {
            log.info("Attempting to load file from path: {}", path);

            // Handle both relative and absolute paths
            Path filePath;
            if (Paths.get(path).isAbsolute()) {
                filePath = Paths.get(path).normalize();
            } else {
                // Check if path already starts with uploadDir to avoid duplication
                if (path.startsWith(uploadDir)) {
                    filePath = Paths.get(path).normalize();
                } else {
                    // If it's a relative path, resolve it against the upload directory
                    filePath = Paths.get(uploadDir).resolve(path).normalize();
                }
            }

            log.info("Resolved file path: {}", filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                log.error("File does not exist at path: {}", filePath.toAbsolutePath());
                throw new RuntimeException("File not found: " + filePath);
            }

            byte[] data = Files.readAllBytes(filePath);
            log.info("Successfully loaded file with size: {} bytes", data.length);

            return data;
        } catch (IOException e) {
            log.error("Failed to read file: {} - {}", path, e.getMessage(), e);
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            log.info("Attempting to delete file at path: {}", path);

            // Handle both relative and absolute paths
            Path filePath;
            if (Paths.get(path).isAbsolute()) {
                filePath = Paths.get(path).normalize();
            } else {
                // Check if path already starts with uploadDir to avoid duplication
                if (path.startsWith(uploadDir)) {
                    filePath = Paths.get(path).normalize();
                } else {
                    // If it's a relative path, resolve it against the upload directory
                    filePath = Paths.get(uploadDir).resolve(path).normalize();
                }
            }

            log.info("Resolved delete path: {}", filePath.toAbsolutePath());

            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("File deleted successfully: {}", filePath);
            } else {
                log.warn("File did not exist for deletion: {}", filePath);
            }
        } catch (Exception e) {
            log.error("Failed to delete file: {} - {}", path, e.getMessage(), e);
            throw new RuntimeException("Failed to delete file: " + path, e);
        }
    }

}
