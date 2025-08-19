package com.gws.crm.common.service.imp;

import com.gws.crm.common.dto.ImageData;
import com.gws.crm.common.dto.ImageMetadata;
import com.gws.crm.common.dto.ImageUploadRequest;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.service.ImageHelperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;


@Service
@Slf4j
public class ImageHelperHelperServiceImp implements ImageHelperService {

    private final String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg"};
    private final String[] allowedMimeTypes = {"image/jpeg", "image/jpg", "image/png", "image/svg+xml", "image/gif",
            "image/webp"};
    @Value("${app.image.upload-path:uploads/images}")
    private String uploadPath;
    @Value("${app.image.base-url:http://localhost:8080}")
    private String baseUrl;
    @Value("${app.image.max-file-size:5242880}") // 5MB default
    private Long maxFileSize;

    public String uploadImage(ImageUploadRequest request, Transition transition) throws Exception {
        try {
            // Extract image data from base64
            ImageData imageData = extractBase64Data(request.getBase64Image());

            // Validate image
            validateImage(imageData.getData(), imageData.getContentType(), request.getFileName());

            // Generate secure filename
            String imageId = generateSecureFileName(request.getFileName(), imageData.getContentType());

            // Ensure upload directory exists
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Save image file to disk
            Path imagePath = uploadDir.resolve(imageId);
            Files.write(imagePath, imageData.getData());

            // Save metadata file
            String metadataFileName = imageId + ".meta";
            Path metadataPath = uploadDir.resolve(metadataFileName);

            ImageMetadata metadata = new ImageMetadata(
                    imageId,
                    imageData.getContentType(),
                    (long) imageData.getData().length,
                    LocalDateTime.now(),
                    transition.getUserName()
            );

            metadata.saveToFile(metadataPath.toString());

            // Create response
            String imageUrl = baseUrl + "/api/images/" + imageId;

            log.info("Image uploaded successfully: {}", imageId);
            return imageUrl;

        } catch (Exception ex) {
            log.error("Error uploading image", ex);
            throw new RuntimeException("Failed to upload image: " + ex.getMessage());
        }
    }

    // Get image as byte array
    public byte[] getImageData(String imageId) throws Exception {
        Path imagePath = Paths.get(uploadPath, imageId);
        if (!Files.exists(imagePath)) {
            throw new RuntimeException("Image not found");
        }
        return Files.readAllBytes(imagePath);
    }

    // Get image as base64 string
    public String getImageBase64(String imageId) throws Exception {
        byte[] imageData = getImageData(imageId);
        ImageMetadata metadata = getImageMetadata(imageId);

        String base64Data = Base64.getEncoder().encodeToString(imageData);
        return "data:" + metadata.getContentType() + ";base64," + base64Data;
    }

    // Get image file for download
    public File getImageFile(String imageId) throws Exception {
        Path imagePath = Paths.get(uploadPath, imageId);
        if (!Files.exists(imagePath)) {
            throw new RuntimeException("Image not found");
        }

        return imagePath.toFile();
    }

    // Get image metadata
    public ImageMetadata getImageMetadata(String imageId) throws Exception {
        String metadataFileName = imageId + ".meta";
        Path metadataPath = Paths.get(uploadPath, metadataFileName);

        if (!Files.exists(metadataPath)) {
            throw new RuntimeException("Image metadata not found");
        }

        return ImageMetadata.loadFromFile(metadataPath.toString());
    }

    // Delete image
    public boolean deleteImage(String imageId) throws Exception {
        Path imagePath = Paths.get(uploadPath, imageId);
        Path metadataPath = Paths.get(uploadPath, imageId + ".meta");

        boolean imageDeleted = false;
        boolean metadataDeleted = false;

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            imageDeleted = true;
        }

        if (Files.exists(metadataPath)) {
            Files.delete(metadataPath);
            metadataDeleted = true;
        }

        if (imageDeleted || metadataDeleted) {
            log.info("Image deleted successfully: {}", imageId);
            return true;
        }

        throw new RuntimeException("Image not found");
    }

    public String extractImageIdFromUrl(String imageUrl) {
        return Paths.get(URI.create(imageUrl).getPath()).getFileName().toString();
    }

    // Check if image exists
    public boolean imageExists(String imageId) {
        Path imagePath = Paths.get(uploadPath, imageId);
        return Files.exists(imagePath);
    }

    private ImageData extractBase64Data(String base64String) {
        String contentType = "image/jpeg"; // default
        String base64Data = base64String;

        // Check if it's a data URL format
        if (base64String.startsWith("data:")) {
            String[] parts = base64String.split(",");
            if (parts.length == 2) {
                String header = parts[0];
                base64Data = parts[1];

                // Extract content type
                String[] headerParts = header.split(";");
                contentType = headerParts[0].replace("data:", "");
            }
        }

        try {
            byte[] data = Base64.getDecoder().decode(base64Data);
            return new ImageData(contentType, data);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid base64 image data");
        }
    }

    private void validateImage(byte[] imageData, String contentType, String originalFileName) {
        // Check file size
        if (imageData.length > maxFileSize) {
            throw new RuntimeException("File size exceeds maximum allowed size of " + (maxFileSize / 1024 / 1024) + "MB");
        }

        // Check content type
        if (!Arrays.asList(allowedMimeTypes).contains(contentType.toLowerCase())) {
            throw new RuntimeException("Content type " + contentType + " is not allowed");
        }

        // Check file extension if filename provided
        if (originalFileName != null && !originalFileName.trim().isEmpty()) {
            String extension = getFileExtension(originalFileName).toLowerCase();
            if (!Arrays.asList(allowedExtensions).contains(extension)) {
                throw new RuntimeException("File extension " + extension + " is not allowed");
            }
        }

        // Validate image file signature
        if (!isValidImageData(imageData)) {
            throw new RuntimeException("Invalid image data - file signature check failed");
        }
    }

    private boolean isValidImageData(byte[] data) {
        if (data.length < 4) return false;

        // Check for common image file signatures
        // JPEG: FF D8 FF
        if (data[0] == (byte) 0xFF && data[1] == (byte) 0xD8 && data[2] == (byte) 0xFF) return true;

        // PNG: 89 50 4E 47
        if (data[0] == (byte) 0x89 && data[1] == (byte) 0x50 && data[2] == (byte) 0x4E && data[3] == (byte) 0x47)
            return true;

        // GIF: 47 49 46 38
        if (data[0] == (byte) 0x47 && data[1] == (byte) 0x49 && data[2] == (byte) 0x46 && data[3] == (byte) 0x38)
            return true;

        // WebP: 52 49 46 46 (RIFF)
        if (data[0] == (byte) 0x52 && data[1] == (byte) 0x49 && data[2] == (byte) 0x46 && data[3] == (byte) 0x46)
            return true;

        String fileStart = new String(data, 0, Math.min(data.length, 100)).toLowerCase();
        return fileStart.contains("<svg") || fileStart.contains("<?xml");
    }

    private String generateSecureFileName(String originalFileName, String contentType) {
        String extension = getExtensionFromContentType(contentType);

        if (originalFileName != null && !originalFileName.trim().isEmpty()) {
            extension = getFileExtension(originalFileName).toLowerCase();
        }

        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        long timestamp = Instant.now().getEpochSecond();

        return timestamp + "_" + uniqueId + extension;
    }

    private String getExtensionFromContentType(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            case "image/svg+xml" -> ".svg";
            default -> ".jpg";
        };
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";
    }

}
