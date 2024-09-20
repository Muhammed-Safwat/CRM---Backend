package com.gws.crm.authentication.controller;

import com.gws.crm.authentication.dto.ResetPasswordDto;
import com.gws.crm.authentication.dto.SignInRequest;
import com.gws.crm.authentication.service.CommonAuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class CommonAuthController {

    private final CommonAuthService commonAuthService;

    @PostMapping("signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return commonAuthService.signIn(signInRequest);
    }

    @GetMapping("check-email/{email}")
    public ResponseEntity<?> isEmailExist(@PathVariable String email) {
        return commonAuthService.isEmailExist(email);
    }


    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPasswordRequest(@Valid @RequestParam String email) throws MessagingException {
        return commonAuthService.forgetPasswordRequest(email);
    }

    @PutMapping("/forget-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) throws MessagingException {
        return commonAuthService.resetPassword(resetPasswordDto);
    }

    @GetMapping("/reset/password/{token}")
    public ResponseEntity<?> isResetPasswordTokenValid(@PathVariable String token) {
        return commonAuthService.isResetPasswordTokenValid(token);
    }


}
