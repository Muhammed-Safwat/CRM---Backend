package com.gws.crm.core.employee.controller;

import com.gws.crm.authentication.dto.ChangePasswordRequest;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.employee.dto.ProfileSettingDTO;
import com.gws.crm.core.employee.service.ProfileSettingService;
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
