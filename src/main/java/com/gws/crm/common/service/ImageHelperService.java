package com.gws.crm.common.service;

import com.gws.crm.common.dto.ImageUploadRequest;
import com.gws.crm.common.entities.Transition;

public interface ImageHelperService {

    String extractImageIdFromUrl(String imageUrl);

    boolean deleteImage(String imageId) throws Exception;

    String uploadImage(ImageUploadRequest request, Transition transition) throws Exception;
}
