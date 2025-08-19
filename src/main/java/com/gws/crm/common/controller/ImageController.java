package com.gws.crm.common.controller;

import com.gws.crm.common.dto.ImageMetadata;
import com.gws.crm.common.dto.ImageUploadRequest;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.service.imp.ImageHelperHelperServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.gws.crm.common.handler.ApiResponseHandler.error;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageHelperHelperServiceImp imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@Valid @RequestBody ImageUploadRequest request, Transition transition) {
        try {
            String path = imageService.uploadImage(request, transition);
            return success(path);
        } catch (Exception ex) {
            return error(ex.getMessage());
        }
    }

    // Get image as binary data (default)
    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageId) {
        try {
            byte[] imageData = imageService.getImageData(imageId);
            ImageMetadata metadata = imageService.getImageMetadata(imageId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(metadata.getContentType()));
            headers.setContentLength(imageData.length);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get image as base64 string
    @GetMapping("/{imageId}/base64")
    public ResponseEntity<?> getImageBase64(@PathVariable String imageId) {
        try {
            String base64Image = imageService.getImageBase64(imageId);
            Map<String, String> response = new HashMap<>();
            response.put("base64Image", base64Image);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Download image as file
    @GetMapping("/{imageId}/download")
    public ResponseEntity<Resource> downloadImage(@PathVariable String imageId) {
        try {
            File imageFile = imageService.getImageFile(imageId);
            ImageMetadata metadata = imageService.getImageMetadata(imageId);

            Resource resource = new FileSystemResource(imageFile);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + metadata.getOriginalFileName() + "\"");
            headers.setContentType(MediaType.parseMediaType(metadata.getContentType()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(imageFile.length())
                    .body(resource);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get image with format parameter
    @GetMapping("/{imageId}/view")
    public ResponseEntity<?> getImageWithFormat(@PathVariable String imageId,
                                                @RequestParam(defaultValue = "binary") String format) {
        try {
            switch (format.toLowerCase()) {
                case "base64":
                    return getImageBase64(imageId);
                case "download":
                    return downloadImage(imageId);
                case "binary":
                default:
                    return getImage(imageId);
            }
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Get image metadata only
    @GetMapping("/{imageId}/info")
    public ResponseEntity<?> getImageInfo(@PathVariable String imageId) {
        try {
            ImageMetadata metadata = imageService.getImageMetadata(imageId);
            return ResponseEntity.ok(metadata);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Check if image exists
    @GetMapping("/{imageId}/exists")
    public ResponseEntity<?> checkImageExists(@PathVariable String imageId) {
        boolean exists = imageService.imageExists(imageId);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("imageId", imageId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable String imageId) {
        try {
            boolean success = imageService.deleteImage(imageId);
            if (success) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Image deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}