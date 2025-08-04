package com.gws.crm.common.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.leads.repository.LeadRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@AllArgsConstructor
public class PhoneValidationService {

    private final LeadRepository leadRepository;

    public ResponseEntity<?> isPhoneExist(List<String> phones, Transition transition) {
        HashMap<String, Object> responseBody = new HashMap<>();
        List<String> existingPhones = new ArrayList<>();

        for (String phone : phones) {
            boolean exists = leadRepository.isPhoneExist(phone);
            if (exists) {
                existingPhones.add(phone);
            }
        }

        if (!existingPhones.isEmpty()) {
            String message = "The following phone numbers already exist: " + String.join(", ", existingPhones);
            responseBody.put("duplicateExists", true);
            responseBody.put("message", message);
        } else {
            responseBody.put("duplicateExists", false);
            responseBody.put("message", "No duplicate phone numbers found.");
        }

        return success(responseBody);
    }


    public ResponseEntity<?> isPhoneExist(String phone, Transition transition) {
        boolean exists = leadRepository.isPhoneExist(phone);
        HashMap<String, Boolean> body = new HashMap<>();
        body.put("isExists", exists);
        return success(body);
    }

}
