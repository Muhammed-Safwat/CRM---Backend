package com.gws.crm.authentication.controller;

import com.gws.crm.authentication.dto.ChangePasswordRequest;
import com.gws.crm.authentication.dto.ProfileSettingDTO;
import com.gws.crm.authentication.service.ProfileSettingService;
import com.gws.crm.common.entities.Transition;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile-setting")
@AllArgsConstructor
public class ProfileSettingController {

    private final ProfileSettingService profileSettingService;

    @GetMapping()
    public ResponseEntity<?> getDetails(Transition transition) {
        return profileSettingService.getDetails(transition);
    }

    @PutMapping()
    public ResponseEntity<?> updateDetails(@RequestBody ProfileSettingDTO profileSettingDTO, Transition transition) {
        return profileSettingService.updateDetails(profileSettingDTO, transition);
    }

    @PutMapping("/change-password")
    private ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                             Transition transition) {
        return profileSettingService.changePassword(changePasswordRequest, transition);
    }
}
