package com.gws.crm.authentication.service;

import com.gws.crm.authentication.dto.ChangePasswordRequest;
import com.gws.crm.authentication.dto.ProfileSettingDTO;
import com.gws.crm.common.entities.Transition;
import org.springframework.http.ResponseEntity;

public interface ProfileSettingService {
    ResponseEntity<?> getDetails(Transition transition);

    ResponseEntity<?> updateDetails(ProfileSettingDTO profileSettingDTO, Transition transition);

    ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest, Transition transition);
}
