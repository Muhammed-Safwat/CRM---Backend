package com.gws.crm.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ImageUploadRequest {
    @NotBlank(message = "Base64 image data is required")
    private String base64Image;
    private String fileName;
}