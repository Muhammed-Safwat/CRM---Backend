package com.gws.crm.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageMetadata {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    private String uploadedBy;

    public static ImageMetadata loadFromFile(String metadataFilePath) throws IOException {
        File file = new File(metadataFilePath);
        if (!file.exists()) {
            return null;
        }
        return objectMapper.readValue(file, ImageMetadata.class);
    }

    // Methods to save/load metadata
    public void saveToFile(String metadataFilePath) throws IOException {
        objectMapper.writeValue(new File(metadataFilePath), this);
    }
}
