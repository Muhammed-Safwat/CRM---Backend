package com.gws.crm.authentication.service;

import com.gws.crm.authentication.dto.ResetPasswordDto;
import com.gws.crm.authentication.dto.SignInRequest;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface CommonAuthService {

    ResponseEntity<?> signIn(SignInRequest signInRequest);

    ResponseEntity<?> isEmailExist(String email);

    ResponseEntity<?> forgetPasswordRequest(String email) throws MessagingException;

    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto);

    ResponseEntity<?> isResetPasswordTokenValid(String token);
}
