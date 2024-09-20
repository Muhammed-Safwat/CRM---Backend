package com.gws.crm.authentication.service.imp;

import com.gws.crm.authentication.config.AuthenticationProviderService;
import com.gws.crm.authentication.dto.ResetPasswordDto;
import com.gws.crm.authentication.dto.SignInRequest;
import com.gws.crm.authentication.dto.SignInResponse;
import com.gws.crm.authentication.entity.ForgetPasswordRequest;
import com.gws.crm.authentication.entity.User;
import com.gws.crm.authentication.repository.ForgetPasswordTokenRepository;
import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.authentication.service.CommonAuthService;
import com.gws.crm.authentication.utils.JwtTokenService;
import com.gws.crm.common.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.gws.crm.common.handler.ApiResponseHandler.error;
import static com.gws.crm.common.handler.ApiResponseHandler.success;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonAuthServiceImp implements CommonAuthService {

    private final AuthenticationProviderService authenticationProviderService;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final ForgetPasswordTokenRepository forgetPasswordTokenRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${reset.password.client.routing}")
    private String clientLink;

    @Override
    public ResponseEntity<?> signIn(SignInRequest signInRequest) {
        try {
            Authentication authentication = authenticationProviderService.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getUsername(),
                    signInRequest.getPassword()
            ));
            if (authentication.isAuthenticated()) {
                Optional<User> userOptional = userRepository.findByUsername(signInRequest.getUsername());
                SignInResponse signInResponse = SignInResponse.builder()
                        .accessToken(jwtTokenService.generateAccessToken(userOptional.get()))
                        .refreshToken(jwtTokenService.generateRefreshToken(userOptional.get()))
                        .build();
                return success(signInResponse);
            }
        } catch (AuthenticationException e) {
            return error(e.getMessage());
        }
        return error("Invalid username or password");
    }

    @Override
    public ResponseEntity<?> isEmailExist(String email) {
        Map<String, Boolean> checkEmail = new HashMap<>();
        checkEmail.put("isExist", userRepository.existsByUsername(email));
        return success(checkEmail);
    }

    @Override
    public ResponseEntity<?> forgetPasswordRequest(String email) throws MessagingException {
        boolean isEmailExists = userRepository.existsByUsername(email);

        if (!isEmailExists) {
            return error("The email address provided does not match any existing accounts.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validTime = now.minusSeconds(90);
        Optional<ForgetPasswordRequest> recentToken = forgetPasswordTokenRepo.findByEmailAndCreatedAtAfter(email, validTime);

        if (recentToken.isPresent()) {
            return error("A password reset request has already been made. Please wait before resending a request.");
        }

        forgetPasswordTokenRepo.deleteByEmail(email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = now.plusMinutes(30);
        ForgetPasswordRequest forgetPasswordRequest = ForgetPasswordRequest.builder()
                .createdAt(now)
                .token(token)
                .email(email)
                .valid(true)
                .expiredDate(expiryDate)
                .build();

        forgetPasswordTokenRepo.save(forgetPasswordRequest);

        String link = clientLink.concat(token);
        log.info("Password reset link: {}", link);
        emailService.sendRestPasswordEmail(email, link);

        return success("A password reset link has been sent to your email address. Please check your inbox.");
    }

    @Override
    @Transactional
    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        Optional<ForgetPasswordRequest> changeReq = forgetPasswordTokenRepo.getByToken(resetPasswordDto.getToken());
        if (changeReq.isEmpty()) {
            return error("Invalid or expired reset token. Please request a new password reset.");
        }

        ForgetPasswordRequest resetRequest = changeReq.get();

        if (resetRequest.getExpiredDate().isBefore(LocalDateTime.now())) {
            return error("The password reset token has expired. Please request a new one.");
        }

        if (!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())) {
            return error("The passwords do not match. Please ensure both password fields are identical.");
        }

        Optional<User> optionalUser = userRepository.findByUsername(resetRequest.getEmail());
        if (optionalUser.isEmpty()) {
            return error("User account not found.");
        }

        User user = optionalUser.get();
        String newPassword = passwordEncoder.encode(resetPasswordDto.getPassword());
        user.setPassword(newPassword);
        userRepository.save(user);

        resetRequest.setValid(false);
        forgetPasswordTokenRepo.save(resetRequest);

        return success("Your password has been successfully reset.");
    }


    @Override
    public ResponseEntity<?> isResetPasswordTokenValid(String token) {
        if (forgetPasswordTokenRepo.existsByTokenAndValidAndExpiredDateAfter(token, true, LocalDateTime.now())) {
            return success();
        }
        return error("Not Valid");
    }

}

